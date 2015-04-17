package com.biit.drools.plugin.configuration;

import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.exception.PropertyNotFoundException;

public class LifeayPluginConfigurationReader extends ConfigurationReader {

	private static LifeayPluginConfigurationReader instance;

	public static LifeayPluginConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (LifeayPluginConfigurationReader.class) {
				if (instance == null) {
					instance = new LifeayPluginConfigurationReader();
				}
			}
		}
		return instance;
	}

	@Override
	public String getProperty(String propertyId) {
		try {
			return super.getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			return null;
		}
	}

	public Integer getArticleId(String propertyTag) {
		String propertyValue = getProperty(propertyTag);
		try {
			return Integer.parseInt(propertyValue);
		} catch (Exception e) {

		}
		return null;
	}

}
