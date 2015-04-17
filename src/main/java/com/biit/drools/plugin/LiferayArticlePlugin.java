package com.biit.drools.plugin;

import java.io.IOException;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.apache.http.client.ClientProtocolException;

import com.biit.drools.plugin.configuration.LifeayPluginConfigurationReader;
import com.biit.liferay.access.KnowledgeBaseService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.model.KbArticle;
import com.biit.plugins.BasePlugin;

@PluginImplementation
public class LiferayArticlePlugin extends BasePlugin {
	private KnowledgeBaseService knowledgeBaseService = new KnowledgeBaseService();

	// Plugin name (must be unique)
	public static String NAME = "LiferayKnowledgeBasePlugin";

	@Override
	public String getPluginName() {
		return NAME;
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
	public String methodGetLatestArticleContent(Double resourcePrimaryKey) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (resourcePrimaryKey != null) {
			KbArticle article = knowledgeBaseService.getLatestArticle(resourcePrimaryKey.longValue());
			if (article != null) {
				return formatArticle(article);
			}
		}
		return "";
	}

	/**
	 * Returns an article by a property value. The property must be mapped in the settings.conf as a unique
	 * identificator with the Liferay primary key. I.e. "Article1=25600"
	 * 
	 * @param propertyTag
	 * @return
	 * @throws ClientProtocolException
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public String methodGetLatestArticleContent(String propertyTag) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		Integer resourcePrimaryKey = LifeayPluginConfigurationReader.getInstance().getArticleId(propertyTag);
		if (resourcePrimaryKey != null) {
			KbArticle article = knowledgeBaseService.getLatestArticle(resourcePrimaryKey.longValue());
			if (article != null) {
				return formatArticle(article);
			}
		}
		return "";
	}

	private String formatArticle(KbArticle article) {
		return article.getTitle() + "\n" + article.getContent();
	}
}
