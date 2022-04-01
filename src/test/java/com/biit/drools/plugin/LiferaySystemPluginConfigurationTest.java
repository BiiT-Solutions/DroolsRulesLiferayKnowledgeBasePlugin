package com.biit.drools.plugin;

import com.biit.drools.plugin.configuration.LiferayPluginConfigurationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Test(groups = {"systemVariable"})
public class LiferaySystemPluginConfigurationTest extends AbstractTestNGSpringContextTests {
    private static Path tempFile;

    static {
        try {
            tempFile = Files.createTempFile("settings", ".conf");
            //Add content to temp file
            List<String> lines = Arrays.asList("Appendix-Antropometrie=54006", "fms-goal-text=54006");
            Files.write(tempFile, lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            //Set the system environment.
            System.setProperty(LiferayPluginConfigurationReader.SYSTEM_VARIABLE, tempFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private LiferayPluginConfigurationReader liferayPluginConfigurationReader;

    @Test
    public void loadFmsFromSystemVariable() throws IOException {
        //Load the content
        Assert.assertEquals(liferayPluginConfigurationReader.getArticleId("fms-goal-text"), Integer.valueOf(44006));
    }
}
