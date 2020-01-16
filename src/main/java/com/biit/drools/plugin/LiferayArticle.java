package com.biit.drools.plugin;

import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.pf4j.Extension;

import com.biit.drools.plugin.configuration.LiferayPluginConfigurationReader;
import com.biit.drools.plugin.log.LiferaryArticlePluginLogger;
import com.biit.liferay.access.ArticleService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.model.IArticle;
import com.biit.plugins.BasePlugin;
import com.biit.plugins.interfaces.IPlugin;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.utils.configuration.IPropertiesSource;

@Extension
public class LiferayArticle extends BasePlugin implements IPlugin {
	private static final String INVALID_ARTICLE_TAG = "<undefined>";
	private static final String ERROR_ARTICLE_TAG = "<error>";
	private static final String NAME = "liferay-article";
	private ArticleService knowledgeBaseService;

	@Override
	public String getPluginName() {
		return NAME;
	}

	public LiferayArticle() {
		super();
		knowledgeBaseService = new ArticleService();
		knowledgeBaseService.serverConnection();
	}

	/**
	 * Gets an article by its Liferay primary key.S
	 * 
	 * @param resourcePrimaryKey Liferay Ids of the article.
	 * @return the article
	 */
	public String methodGetLatestArticleContent(Double resourcePrimaryKey) {
		if (resourcePrimaryKey != null) {
			try {
				IArticle<Long> article = knowledgeBaseService.getLatestArticle(resourcePrimaryKey.longValue());
				LiferaryArticlePluginLogger.debug(this.getClass().getName(), "Article retrieved '" + article + "'.");
				if (article != null) {
					return formatArticle(article);
				}
			} catch (NotConnectedToWebServiceException | ClientProtocolException | AuthenticationRequired
					| WebServiceAccessError e) {
				LiferaryArticlePluginLogger.severe(this.getClass().getName(),
						"Article '" + resourcePrimaryKey + "' not found!");
				LiferaryArticlePluginLogger.errorMessage(this.getClass().getName(), e);
				return ERROR_ARTICLE_TAG;
			} catch (Exception e) {
				LiferaryArticlePluginLogger.severe(this.getClass().getName(),
						"Error retrieving article with id '" + resourcePrimaryKey + "'.");
				LiferaryArticlePluginLogger.errorMessage(this.getClass().getName(), e);
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
			LiferaryArticlePluginLogger.debug(this.getClass().getName(), "Getting article for '" + propertyTag + "'.");
			Integer resourcePrimaryKey = LiferayPluginConfigurationReader.getInstance().getArticleId(propertyTag);
			LiferaryArticlePluginLogger.info(this.getClass().getName(),
					"Primary key retrieved for '" + propertyTag + "' is '" + resourcePrimaryKey + "'.");
			if (resourcePrimaryKey == null) {
				return INVALID_ARTICLE_TAG;
			}
			return methodGetLatestArticleContent((double) resourcePrimaryKey);
		} catch (Exception e) {
			LiferaryArticlePluginLogger.errorMessage(this.getClass().getName(), e);
			return INVALID_ARTICLE_TAG;
		}
	}

	private String formatArticle(IArticle<Long> article) {
		if (LiferayPluginConfigurationReader.getInstance().isArticleHeaderEnabled()) {
			return article.getTitle() + "\n" + article.getContent();
		} else {
			return article.getContent();
		}
	}

	/**
	 * Enable the properties list to be use as a plugin.
	 * 
	 * @return gets all properties sources.
	 */
	public List<IPropertiesSource> methodGetPropertiesSources() {
		return LiferayPluginConfigurationReader.getInstance().getPropertiesSources();
	}
}
