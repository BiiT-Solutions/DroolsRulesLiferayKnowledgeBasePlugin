package com.biit.drools.plugin;

import com.biit.plugins.configuration.PluginConfigurationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Test(groups = {"folderSettings"})
public class LiferaySameFolderPluginConfigurationTest extends AbstractTestNGSpringContextTests {
    private static File tempFile;

    static {
        try {
            tempFile = File.createTempFile("settings", ".conf");
            tempFile.deleteOnExit();
            //Add content to temp file
            List<String> lines = Arrays.asList("Appendix-Antropometrie2=54006", "fms-goal-text2=54006");
            Files.write(tempFile.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private PluginConfigurationReader pluginConfigurationReader;

    @Test
    public void loadFmsFromSystemVariable() {
        //Load the content
        Assert.assertEquals(pluginConfigurationReader.getProperty("fms-goal-text2"), "54006");
    }
}
