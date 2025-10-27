package com.biit.drools.plugin;

/*-
 * #%L
 * Liferay Knowledge Base Access For Drools Rules
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
@Test(groups = {"systemVariable"})
public class LiferaySystemPluginConfigurationTest extends AbstractTestNGSpringContextTests {
    private static File tempFile;

    static {
        try {
            tempFile = File.createTempFile("settings", ".conf");
            tempFile.deleteOnExit();
            //Add content to temp file
            List<String> lines = Arrays.asList("Appendix-Antropometrie2=54006", "fms-goal-text2=54006");
            Files.write(tempFile.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            //Set the system environment.
            System.setProperty(PluginConfigurationReader.SYSTEM_VARIABLE_PLUGINS_CONFIG_FOLDER, tempFile.getParent());
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
