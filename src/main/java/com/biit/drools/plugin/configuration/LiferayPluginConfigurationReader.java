package com.biit.drools.plugin.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.biit.logger.BiitCommonLogger;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exception.PropertyNotFoundException;

public class LiferayPluginConfigurationReader extends ConfigurationReader {
	private static final String SETTINGS_FILE = "settings.conf";
	private static final String SYSTEM_VARIABLE = "LIFERAY_PLUGIN_CONFIG";

	private static LiferayPluginConfigurationReader instance;

	private URL getJarUrl() {
		URL url = this.getClass().getResource('/' + this.getClass().getName().replace('.', '/') + ".class");
		if (url == null) {
			return null;
		}
		// Remove class inside JAR file (i.e. jar:file:///outer.jar!/file.class)
		String packetPath = url.getPath();
		if (packetPath.contains("!")) {
			packetPath = packetPath.substring(0, packetPath.indexOf("!"));
		}

		packetPath = packetPath.replace("jar:", "");
		try {
			url = new URL(packetPath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			BiitCommonLogger.errorMessageNotification(this.getClass(), e);
		}

		return url;
	}

	private String getJarFolder() {
		URL settingsUrl = getJarUrl();
		return settingsUrl.getPath().substring(0, settingsUrl.getPath().lastIndexOf('/'));
	}

	private String getJarName() {
		URL settingsUrl = getJarUrl();
		if (settingsUrl == null || !settingsUrl.getPath().contains(".jar")) {
			return null;
		}
		return settingsUrl.getPath().substring(settingsUrl.getPath().lastIndexOf('/') + 1,
				settingsUrl.getPath().length() - ".jar".length());
	}

	public LiferayPluginConfigurationReader() {
		BiitCommonLogger.debug(this.getClass(), "Loading default settings file...");
		addPropertiesSource(new PropertiesSourceFile(SETTINGS_FILE));

		String settingsFile = getJarName();
		BiitCommonLogger.debug(this.getClass(), "Loading settings file " + settingsFile);
		// Load settings as resource.
		if (settingsFile != null) {
			// using same name as jar file.
			if (resourceExist(settingsFile + ".conf")) {
				addPropertiesSource(new PropertiesSourceFile(settingsFile + ".conf"));
				BiitCommonLogger.debug(this.getClass(), "Plugin using settings in resource folder '" + settingsFile + ".conf" + "'.");
			}
		}
		// Load settings as file.
		settingsFile = getJarFolder() + "/" + getJarName() + ".conf";
		BiitCommonLogger.debug(this.getClass(), "Searching for configuration file in '" + settingsFile + "'.");
		if (fileExists(settingsFile)) {
			addPropertiesSource(new PropertiesSourceFile(getJarFolder(), getJarName() + ".conf"));
			BiitCommonLogger.debug(this.getClass(), "Found configuration file '" + settingsFile + "'!");
		}

		// Load folder in system variables.
		addPropertiesSource(new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE, SETTINGS_FILE));
	}

	private boolean fileExists(String filePathString) {
		File f = new File(filePathString);
		return (f.exists() && !f.isDirectory());
	}

	private boolean resourceExist(String resourceName) {
		URL u = LiferayPluginConfigurationReader.class.getClassLoader().getResource(resourceName);
		return u != null;
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
