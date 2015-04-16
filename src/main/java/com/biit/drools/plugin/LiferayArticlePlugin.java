package com.biit.drools.plugin;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.KnowledgeBaseService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.model.KbArticle;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class LiferayArticlePlugin {

	private KnowledgeBaseService knowledgeBaseService = new KnowledgeBaseService();

	/**
	 * All methods of a plugin must starts with the prefix "method".
	 * 
	 * @param parameter1
	 * @param parameter2
	 * @return
	 * @throws WebServiceAccessError
	 * @throws AuthenticationRequired
	 * @throws IOException
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 */
	public KbArticle methodGetLatestArticle(long resourcePrimaryKey) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		return knowledgeBaseService.getLatestArticle(resourcePrimaryKey);
	}

}
