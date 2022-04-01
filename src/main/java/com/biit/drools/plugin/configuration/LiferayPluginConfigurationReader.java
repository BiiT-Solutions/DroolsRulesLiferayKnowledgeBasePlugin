package com.biit.drools.plugin.configuration;

import com.biit.drools.plugin.log.LiferayArticlePluginLogger;
import com.biit.plugins.configuration.PluginConfigurationReader;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;

@Component
public class LiferayPluginConfigurationReader extends PluginConfigurationReader implements EmbeddedValueResolverAware {
    public static final String SYSTEM_VARIABLE = "LIFERAY_PLUGIN_CONFIG";
    private static final String ID_INCLUDE_ARTICLE_HEADER = "article.header";

    public LiferayPluginConfigurationReader() {
        super(LiferayPluginConfigurationReader.class);
        // Load folder in system variables.
//        addPropertiesSource(new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE, SETTINGS_FILE));
//        addPropertiesSource(new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE, getJarName() + ".conf"));
    }


    public Integer getArticleId(String propertyTag) {
        //Check if property exists.
        String propertyValue = getProperty(propertyTag);
        try {
            return Integer.parseInt(propertyValue);
        } catch (NumberFormatException e) {
            LiferayArticlePluginLogger.warning(this.getClass().getName(),
                    "Invalid number '" + propertyValue + "' for property '" + propertyTag + "'.");
        } catch (Exception e) {
            LiferayArticlePluginLogger.warning(this.getClass().getName(),
                    "Invalid property '" + propertyTag + "' or not found in any settings file.");
        }

        return null;
    }

    public boolean isArticleHeaderEnabled() {
        return Boolean.parseBoolean(getProperty(ID_INCLUDE_ARTICLE_HEADER));
    }

    @Override
    protected String getSystemVariableWithPath() {
        return SYSTEM_VARIABLE;
    }
}
