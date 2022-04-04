package com.biit.drools.plugin;

import com.biit.drools.plugin.log.LiferayArticlePluginLogger;
import com.biit.liferay.access.ArticleService;
import com.biit.liferay.access.exceptions.ArticleNotFoundException;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.model.IArticle;
import com.biit.plugins.BasePlugin;
import com.biit.plugins.configuration.PluginConfigurationReader;
import com.biit.plugins.interfaces.IPlugin;
import com.biit.plugins.logger.PluginManagerLogger;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.utils.configuration.IPropertiesSource;
import org.apache.http.client.ClientProtocolException;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Extension
public class LiferayArticle extends BasePlugin implements IPlugin {
    private static final String ID_INCLUDE_ARTICLE_HEADER = "article.header";
    private static final String INVALID_ARTICLE_TAG = "<undefined>";
    private static final String ERROR_ARTICLE_TAG = "<error>";
    private static final String NAME = "liferay-article";
    private ArticleService knowledgeBaseService;

    private final PluginConfigurationReader pluginConfigurationReader;

    @Override
    public String getPluginName() {
        return NAME;
    }

    @Autowired
    public LiferayArticle(PluginConfigurationReader pluginConfigurationReader) {
        super();
        this.pluginConfigurationReader = pluginConfigurationReader;
        knowledgeBaseService = new ArticleService();
        knowledgeBaseService.serverConnection();
    }

    public String methodGetLatestArticleContent(Double resourcePrimaryKey) {
        return methodGetLatestArticleContent(resourcePrimaryKey.intValue());
    }


    /**
     * Gets an article by its Liferay primary key.
     *
     * @param resourcePrimaryKey Liferay Ids of the article.
     * @return the article
     */
    public String methodGetLatestArticleContent(Integer resourcePrimaryKey) {
        if (resourcePrimaryKey != null) {
            try {
                IArticle<Long> article = knowledgeBaseService.getLatestArticle(resourcePrimaryKey);
                LiferayArticlePluginLogger.debug(this.getClass().getName(), "Article retrieved '" + article + "'.");
                if (article != null) {
                    return formatArticle(article);
                }
            } catch (NotConnectedToWebServiceException | ClientProtocolException | AuthenticationRequired
                    | WebServiceAccessError e) {
                LiferayArticlePluginLogger.severe(this.getClass().getName(),
                        "Article '" + resourcePrimaryKey + "' not found!");
                LiferayArticlePluginLogger.errorMessage(this.getClass().getName(), e);
                return ERROR_ARTICLE_TAG;
            } catch (ArticleNotFoundException e) {
                LiferayArticlePluginLogger.severe(this.getClass().getName(),
                        "Article with id '" + resourcePrimaryKey + "' not found.");
                return String.format("<Article with id '%d' not found on Liferay>", resourcePrimaryKey);
            } catch (Exception e) {
                LiferayArticlePluginLogger.severe(this.getClass().getName(),
                        "Error retrieving article with id '" + resourcePrimaryKey + "'.");
                LiferayArticlePluginLogger.errorMessage(this.getClass().getName(), e);
                return ERROR_ARTICLE_TAG;
            }
        }
        return INVALID_ARTICLE_TAG;
    }

    /**
     * Returns an article by a property value. The property must be mapped in the
     * settings.conf as a unique identificator with the Liferay primary key. I.e.
     * "Article1=25600"
     *
     * @param propertyTag a string that identifies the article.
     * @return the article text.
     */
    public String methodGetLatestArticleContentByProperty(String propertyTag) {
        try {
            LiferayArticlePluginLogger.debug(this.getClass().getName(), "Getting article for '" + propertyTag + "'.");
            Integer resourcePrimaryKey = getArticleId(propertyTag);
            LiferayArticlePluginLogger.info(this.getClass().getName(),
                    "Primary key retrieved for '" + propertyTag + "' is '" + resourcePrimaryKey + "'.");
            if (resourcePrimaryKey == null) {
                return INVALID_ARTICLE_TAG;
            }
            return methodGetLatestArticleContent(resourcePrimaryKey);
        } catch (Exception e) {
            LiferayArticlePluginLogger.errorMessage(this.getClass().getName(), e);
            return INVALID_ARTICLE_TAG;
        }
    }

    private String formatArticle(IArticle<Long> article) {
        if (isArticleHeaderEnabled()) {
            return article.getTitle() + "\n" + article.getContent();
        } else {
            return article.getContent();
        }
    }

    private Integer getArticleId(String articleTag) {
        //Check if property exists.
        try {
            String propertyValue = pluginConfigurationReader.getPropertyValue(articleTag);
            try {
                return Integer.parseInt(propertyValue);
            } catch (NumberFormatException e) {
                PluginManagerLogger.warning(this.getClass().getName(),
                        "Invalid number '" + propertyValue + "' for article '" + articleTag + "'.");
            } catch (Exception e) {
                PluginManagerLogger.warning(this.getClass().getName(),
                        "Invalid article '" + articleTag + "' or not found in any settings file.");
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return null;
    }

    public boolean isArticleHeaderEnabled() {
        return Boolean.parseBoolean(pluginConfigurationReader.getPropertyValue(ID_INCLUDE_ARTICLE_HEADER));
    }
}
