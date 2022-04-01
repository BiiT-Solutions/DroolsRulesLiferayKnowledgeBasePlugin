package com.biit.drools.plugin;

import com.biit.drools.plugin.configuration.LiferayPluginConfigurationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest
@Test(groups = { "liferayPluginsTest" })
public class LiferayPluginConfigurationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private LiferayPluginConfigurationReader liferayPluginConfigurationReader;

    @Test
    public void loadFms(){
        Assert.assertEquals(liferayPluginConfigurationReader.getArticleId("fms-goal-text"), Integer.valueOf(44006));
    }
}
