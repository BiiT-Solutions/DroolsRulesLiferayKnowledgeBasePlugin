package com.biit.drools.plugin.configuration;

import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.exception.PropertyNotFoundException;

public class LifeayPluginConfigurationReader extends ConfigurationReader {
	private static final String SETTINGS_FILE = "settings.conf";

	private static LifeayPluginConfigurationReader instance;

	public LifeayPluginConfigurationReader() {
		addPropertiesSource(new PropertiesSourceFile(SETTINGS_FILE));
	}

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

	public Integer getArticleId(String propertyTag) {
		//Add property in property list to allow the use.
		addProperty(propertyTag, null);
		//Force to load all files to find the new property
		readConfigurations();
		try {
			String propertyValue = getProperty(propertyTag);
			try {
				return Integer.parseInt(propertyValue);
			} catch (Exception e) {

			}
		} catch (PropertyNotFoundException e) {
			return null;
		}
		return null;
	}

}
