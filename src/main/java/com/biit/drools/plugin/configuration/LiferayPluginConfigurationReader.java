package com.biit.drools.plugin.configuration;

import com.biit.liferay.log.LiferayClientLogger;
import com.biit.logger.BiitCommonLogger;
import com.biit.plugins.configuration.PluginConfigurationReader;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;

public class LiferayPluginConfigurationReader extends PluginConfigurationReader {
	private static final String SYSTEM_VARIABLE = "LIFERAY_PLUGIN_CONFIG";

	private static final String ID_INCLUDE_ARTICLE_HEADER = "article.header";
	private static final String DEFAULT_INCLUDE_ARTICLE_HEADER = "false";

	private static LiferayPluginConfigurationReader instance;

	public LiferayPluginConfigurationReader() {
		super(LiferayPluginConfigurationReader.class);

		addProperty(ID_INCLUDE_ARTICLE_HEADER, DEFAULT_INCLUDE_ARTICLE_HEADER);

		// Load folder in system variables.
		addPropertiesSource(new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE, SETTINGS_FILE));
		addPropertiesSource(new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE, getJarName() + ".conf"));
	}

	public static LiferayPluginConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (LiferayPluginConfigurationReader.class) {
				if (instance == null) {
					instance = new LiferayPluginConfigurationReader();
				}
			}
		}
		return instance;
	}

	private String getPropertyLogException(String propertyId) {
		try {
			return getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			BiitCommonLogger.errorMessageNotification(this.getClass(), e);
			return null;
		}
	}

	public Integer getArticleId(String propertyTag) {
		// Add property in property list to allow the use.
		addProperty(propertyTag, null);
		// Force to load all files to find the new property
		readConfigurations();
		try {
			String propertyValue = getProperty(propertyTag);
			try {
				return Integer.parseInt(propertyValue);
			} catch (Exception e) {
				LiferayClientLogger.warning(this.getClass().getName(), "Invalid number '" + propertyValue + "' for property '" + propertyTag + "'.");
			}
		} catch (PropertyNotFoundException e) {
			LiferayClientLogger.warning(this.getClass().getName(), "Property " + propertyTag + "' not found!.");
			return null;
		}
		return null;
	}

	public boolean isArticleHeaderEnabled() {
		return Boolean.parseBoolean(getPropertyLogException(ID_INCLUDE_ARTICLE_HEADER));
	}
}
