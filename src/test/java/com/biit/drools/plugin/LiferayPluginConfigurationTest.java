package com.biit.drools.plugin;

import com.biit.plugins.configuration.PluginConfigurationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest
@Test(groups = {"liferayPluginsTest"})
public class LiferayPluginConfigurationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private PluginConfigurationReader pluginConfigurationReader;

    @Test
    public void loadFms() {
        Assert.assertEquals(pluginConfigurationReader.getProperty("fms-goal-text"), "44006");
    }
}
