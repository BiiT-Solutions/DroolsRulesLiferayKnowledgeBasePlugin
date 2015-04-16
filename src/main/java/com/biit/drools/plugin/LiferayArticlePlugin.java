package com.biit.drools.plugin;

import java.io.IOException;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.apache.http.client.ClientProtocolException;

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

	public String methodGetLatestArticleContent(Double resourcePrimaryKey) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (resourcePrimaryKey != null) {
			KbArticle article = knowledgeBaseService.getLatestArticle(resourcePrimaryKey.longValue());
			if (article != null) {
				return article.getTitle() + "\n" + article.getContent();
			}
		}
		return "";
	}
}
