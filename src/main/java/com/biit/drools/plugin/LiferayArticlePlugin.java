package com.biit.drools.plugin;

import java.io.IOException;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.apache.http.client.ClientProtocolException;

import com.biit.drools.plugin.configuration.LiferayPluginConfigurationReader;
import com.biit.liferay.access.ArticleService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.liferay.model.IArticle;
import com.biit.plugins.BasePlugin;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.utils.configuration.IPropertiesSource;

@PluginImplementation
public class LiferayArticlePlugin extends BasePlugin {
	private ArticleService knowledgeBaseService;

	// Plugin name (must be unique)
	public static String NAME = "LiferayKnowledgeBasePlugin";

	@Override
	public String getPluginName() {
		return NAME;
	}

	public LiferayArticlePlugin() {
		super();
		knowledgeBaseService = new ArticleService();
	}

	/**
	 * Gets an article by its Liferay primary key.
	 * 
	 * @param resourcePrimaryKey
	 * @return
	 * @throws ClientProtocolException
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public String methodGetLatestArticleContent(Double resourcePrimaryKey) throws ClientProtocolException, NotConnectedToWebServiceException, IOException,
			AuthenticationRequired, WebServiceAccessError {
		if (resourcePrimaryKey != null) {
			try {
				IArticle<Long> article = knowledgeBaseService.getLatestArticle(resourcePrimaryKey.longValue());
				if (article != null) {
					return formatArticle(article);
				}
			} catch (Exception e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				return e.getMessage();
			}
		}
		return "";
	}

	/**
	 * Returns an article by a property value. The property must be mapped in
	 * the settings.conf as a unique identificator with the Liferay primary key.
	 * I.e. "Article1=25600"
	 * 
	 * @param propertyTag
	 * @return
	 * @throws ClientProtocolException
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public String methodGetLatestArticleContentByProperty(String propertyTag) throws ClientProtocolException, NotConnectedToWebServiceException, IOException,
			AuthenticationRequired, WebServiceAccessError {
		LiferayClientLogger.debug(this.getClass().getName(), "Getting article for: '" + propertyTag + "'.");
		Integer resourcePrimaryKey = LiferayPluginConfigurationReader.getInstance().getArticleId(propertyTag);
		LiferayClientLogger.debug(this.getClass().getName(), "Primary key retrieved is: '" + resourcePrimaryKey + "'.");
		return methodGetLatestArticleContent((double) resourcePrimaryKey);
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
	 */
	public List<IPropertiesSource> methodGetPropertiesSources() {
		return LiferayPluginConfigurationReader.getInstance().getPropertiesSources();
	}
}
