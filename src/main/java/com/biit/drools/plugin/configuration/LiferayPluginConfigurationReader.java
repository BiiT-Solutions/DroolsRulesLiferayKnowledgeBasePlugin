package com.biit.drools.plugin.configuration;

import com.biit.plugins.configuration.PluginConfigurationReader;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exception.PropertyNotFoundException;

public class LiferayPluginConfigurationReader extends PluginConfigurationReader {
	private static final String SYSTEM_VARIABLE = "LIFERAY_PLUGIN_CONFIG";

	private static LiferayPluginConfigurationReader instance;

	public LiferayPluginConfigurationReader() {
		super(LiferayPluginConfigurationReader.class);

		// Load folder in system variables.
		addPropertiesSource(new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE, SETTINGS_FILE));
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

			}
		} catch (PropertyNotFoundException e) {
			return null;
		}
		return null;
	}

}
