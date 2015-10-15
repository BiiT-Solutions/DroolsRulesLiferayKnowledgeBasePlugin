package com.biit.drools.plugin;

import java.io.IOException;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.apache.http.client.ClientProtocolException;

import com.biit.drools.plugin.configuration.LiferayPluginConfigurationReader;
import com.biit.liferay.access.KnowledgeBaseService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.model.KbArticle;
import com.biit.plugins.BasePlugin;
import com.biit.utils.configuration.IPropertiesSource;

@PluginImplementation
public class LiferayArticlePlugin extends BasePlugin {
	private KnowledgeBaseService knowledgeBaseService;

	// Plugin name (must be unique)
	public static String NAME = "LiferayKnowledgeBasePlugin";

	@Override
	public String getPluginName() {
		return NAME;
	}

	public LiferayArticlePlugin() {
		super();
		knowledgeBaseService = new KnowledgeBaseService();
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
				KbArticle article = knowledgeBaseService.getLatestArticle(resourcePrimaryKey.longValue());
				if (article != null) {
					return formatArticle(article);
				}
			} catch (Exception e) {
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
		Integer resourcePrimaryKey = LiferayPluginConfigurationReader.getInstance().getArticleId(propertyTag);
		return methodGetLatestArticleContent((double) resourcePrimaryKey);
	}

	private String formatArticle(KbArticle article) {
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
