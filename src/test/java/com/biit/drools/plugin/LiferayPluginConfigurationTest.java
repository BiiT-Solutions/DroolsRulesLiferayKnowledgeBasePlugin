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
