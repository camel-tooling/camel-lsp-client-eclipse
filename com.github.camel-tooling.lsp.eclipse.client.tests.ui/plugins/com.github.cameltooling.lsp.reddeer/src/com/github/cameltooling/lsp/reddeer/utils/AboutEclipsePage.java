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
package com.github.cameltooling.lsp.reddeer.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.table.DefaultTableItem;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;

/**
 * Represents <i>About Eclipse</i> page and subpages
 * 
 * @author fpospisi
 */
public class AboutEclipsePage {

	public static final String ABOUT_PLATFORM = "About Eclipse Platform";
	public static final String ECLIPSE_INSTALLATION_DETAILS = "Eclipse Platform Installation Details";
	public static final String INSTALLATION_DETAILS = "Installation Details";
	
	/**
	 * Opens About Eclipse page.
	 */
	public static void open() {
		new ShellMenuItem(new WorkbenchShell(), "Window", "Navigation", "Find Actions").select();
		Shell shell = new DefaultShell("Find Actions");
		new DefaultText(shell, 0).setText("About");
		new DefaultTableItem().click();
		new WaitUntil(new ShellIsAvailable(ABOUT_PLATFORM), TimePeriod.DEFAULT);
	}

	/**
	 * Closes opened About Eclipse page.
	 */
	public static void close() {
		Shell shell = new DefaultShell(ABOUT_PLATFORM);
		new PushButton(shell, "Close").click();
	}

	/**
	 * Opens Installation Details page.
	 */
	public static void openInstallationDetails() {
		open();
		Shell shell = new DefaultShell(ABOUT_PLATFORM);
		new PushButton(shell, INSTALLATION_DETAILS).click();
		new WaitUntil(new ShellIsAvailable(ECLIPSE_INSTALLATION_DETAILS), TimePeriod.DEFAULT);
	}

	/**
	 * Closes Installation Details page.
	 */
	public static void closeInstallationDetails() {
		Shell shell = new DefaultShell(ECLIPSE_INSTALLATION_DETAILS);
		new PushButton(shell, "Close").click();
		close();
	}

	/**
	 * Opens page with installed Plug-Ins.
	 */
	public static void openPlugins() {
		openInstallationDetails();
		new WaitUntil(new ShellIsAvailable(ECLIPSE_INSTALLATION_DETAILS), TimePeriod.DEFAULT);
		new DefaultCTabItem("Plug-ins").activate();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	/**
	 * Closes page with installed Plug-Ins.
	 */
	public static void closePlugins() {
		Shell shell = new DefaultShell(ECLIPSE_INSTALLATION_DETAILS);
		new PushButton(shell, "Close").click();
		close();
	}

	/**
	 * Gets list of installed plug-ins.
	 * 
	 * @return List containing installed plug-ins.
	 */
	public static List<TableItem> getPlugins() {
		List<TableItem> plugins = new ArrayList<TableItem>();
		for (TableItem item : new DefaultTable().getItems()) {
			plugins.add(item);
		}
		return plugins;
	}

	/**
	 * Gets first plug-in by entered provider.
	 *
	 * @param provider of searched plugin
	 * @return plugin or null
	 */
	public static TableItem getPluginByProvider(String provider) {
		List<TableItem> plugins = getPlugins();
		for (TableItem plugin : plugins) {
			if (plugin.getText(1).equals(provider)) {
				return plugin;
			}
		}
		return null;
	}

	/**
	 * Gets n-th plug-in by entered provider.
	 *
	 * @param provider of searched plugin
	 * @param ord      order of plugin in list
	 * @return plugin or null
	 */
	public static TableItem getPluginByProvider(String provider, int ord) {
		int counter = 0;
		List<TableItem> plugins = getPlugins();
		for (TableItem plugin : plugins) {
			if (plugin.getText(1).equals(provider)) {
				if (counter != ord) {
					counter++;
				} else {
					return plugin;
				}
			}
		}
		return null;
	}

	/**
	 * Gets first plug-in by entered name.
	 *
	 * @param name of searched plugin
	 * @return plugin or null
	 */
	public static TableItem getPluginByName(String name) {
		List<TableItem> plugins = getPlugins();
		for (TableItem plugin : plugins) {
			if (plugin.getText(2).equals(name)) {
				return plugin;
			}
		}
		return null;
	}

	/**
	 * Gets first plug-in by entered name.
	 *
	 * @param name of searched plugin
	 * @param ord  order of plugin in list
	 * @return plugin or null
	 */
	public static TableItem getPluginByName(String name, int ord) {
		int counter = 0;
		List<TableItem> plugins = getPlugins();
		for (TableItem plugin : plugins) {
			if (plugin.getText(2).equals(name)) {
				if (counter != ord) {
					counter++;
				} else {
					return plugin;
				}
			}
		}
		return null;
	}

	/**
	 * Gets first plug-in by entered id.
	 *
	 * @param id of searched plugin
	 * @return plugin or null
	 */
	public static TableItem getPluginByID(String id) {
		List<TableItem> plugins = getPlugins();
		for (TableItem plugin : plugins) {
			if (plugin.getText(4).equals(id)) {
				return plugin;
			}
		}
		return null;
	}

	/**
	 * Gets first plug-in by entered id.
	 *
	 * @param id  of searched plugin
	 * @param ord order of plugin in list
	 * @return plugin or null
	 */
	public static TableItem getPluginByID(String id, int ord) {
		int counter = 0;
		List<TableItem> plugins = getPlugins();
		for (TableItem plugin : plugins) {
			if (plugin.getText(4).equals(id)) {
				if (counter != ord) {
					counter++;
				} else {
					return plugin;
				}
			}
		}
		return null;
	}

	/**
	 * Gets provider of entered plugin.
	 *
	 * @param plugin
	 * @return provider of entered plugin
	 */
	public static String getPluginProvider(TableItem plugin) {
		return plugin.getText(1);

	}

	/**
	 * Gets name of entered plugin.
	 *
	 * @param plugin
	 * @return name of entered plugin
	 */
	public static String getPluginName(TableItem plugin) {
		return plugin.getText(2);

	}

	/**
	 * Gets version of entered plugin.
	 *
	 * @param version
	 * @return version of entered plugin
	 */
	public static String getPluginVersion(TableItem plugin) {
		return plugin.getText(3);

	}

	/**
	 * Gets id of entered plugin.
	 *
	 * @param id
	 * @return id of entered plugin
	 */
	public static String getPluginID(TableItem plugin) {
		return plugin.getText(4);

	}
}
