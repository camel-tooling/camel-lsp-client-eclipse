/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cameltooling.lsp.ui.tests;

import static org.junit.Assert.*;

import com.github.cameltooling.lsp.reddeer.utils.AboutEclipsePage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;

import org.eclipse.reddeer.swt.api.TableItem;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author fpospisi
 */
@RunWith(RedDeerSuite.class)
public class PluginInstalledTest extends DefaultTest {

	public static final String provider = "JBoss by Red Hat";
	public static final String name = "Apache Camel LSP Client Extensions Plugin";
	public static final String version = "1.0.0";
	public static final String id = "com.github.camel-tooling.eclipse.client";

	/**
	 * Checks if LSP plugin has correct author, name, version and id.
	 */
	@Test
	public void testPluginInstalled() {

		// Open plug-ins page and get plugin.
		AboutEclipsePage.openPlugins();
		TableItem plugin = AboutEclipsePage.getPluginByID(id);

		// Checks id, if null = no plug-in with this id found.
		assertTrue(plugin != null);
		// Check provider.
		assertEquals(AboutEclipsePage.getPluginProvider(plugin), provider);
		// Check name.
		assertEquals(AboutEclipsePage.getPluginName(plugin), name);
		// Check version.
		assertThat(AboutEclipsePage.getPluginVersion(plugin), containsString(version));

		// Close plug-ins page.
		AboutEclipsePage.closePlugins();
	}
}