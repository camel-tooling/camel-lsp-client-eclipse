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

import com.github.cameltooling.lsp.reddeer.utils.CreateNewEmptyFile;
import com.github.cameltooling.lsp.reddeer.utils.JavaProjectFactory;
import com.github.cameltooling.lsp.ui.tests.utils.EditorManipulator;

import org.eclipse.reddeer.common.properties.RedDeerProperties;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.cleanerrorlog.CleanErrorLogRequirement;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;

import com.github.cameltooling.lsp.reddeer.editor.SourceEditor;
import com.github.cameltooling.lsp.reddeer.preference.CamelCatalogVersion;

/*
*
* @author fpospisi
*/
@RunWith(RedDeerSuite.class)
public class CamelCatalogVersionFeatureTest {

	public static final String PROJECT_NAME = "catalog-feature-test";
	public static final String CAMEL_CONTEXT = "camel-context.xml";
	public static final String RESOURCES_CONTEXT_PATH = "resources/catalog-version-feature-context.xml";
	public static final String COMPONENT = "file-watch";
	public static final String COMPONENT_PROPOSAL = "file-watch:path";
	public static final String DEFAULT_VERSION = "";
	public static final String OLDER_VERSION = "2.15.1";

	public static final String SOURCE_TAB = "Source";

	private SourceEditor sourceEditor;
	private int cursorPosition;

	private String timePeriodfactor;
	private static final String TIMEOUT_PERIOD_FACTOR_PROPETY_NAME = RedDeerProperties.TIME_PERIOD_FACTOR.getName();

	/**
	 * Creates empty project, then creates XML file with camel-context.
	 */
	@BeforeClass
	public static void setupTestEnvironment() {
		JavaProjectFactory.create(PROJECT_NAME);
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		CreateNewEmptyFile.XMLFile(CAMEL_CONTEXT);
		DefaultEditor editor = new DefaultEditor(CAMEL_CONTEXT);
		editor.activate();
		new DefaultCTabItem(SOURCE_TAB).activate();
		EditorManipulator.copyFileContentToXMLEditor(RESOURCES_CONTEXT_PATH);
	}

	/**
	 * Prepares test environment.
	 */
	@Before
	public void defaultBeforeTestSetup() {
		new CleanErrorLogRequirement().fulfill();
	}

	@Before
	public void setupTimeout() {
		timePeriodfactor = System.getProperty(TIMEOUT_PERIOD_FACTOR_PROPETY_NAME);
		System.setProperty(TIMEOUT_PERIOD_FACTOR_PROPETY_NAME, "3");
		TimePeriod.updateFactor();
	}

	/**
	 * Cleans up test environment.
	 */
	@After
	public void defaultAfterTestClean() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	@After
	public void tearDown() {
		if (timePeriodfactor != null) {
			System.setProperty(TIMEOUT_PERIOD_FACTOR_PROPETY_NAME, timePeriodfactor);
		} else {
			System.clearProperty(TIMEOUT_PERIOD_FACTOR_PROPETY_NAME);
		}
		TimePeriod.updateFactor();

		setCamelCatalogVersion(DEFAULT_VERSION); // set version back to default
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	/**
	 * Checks if proposal is available for component in latest catalog. Then changes
	 * version of used Catalog to older one which is not containing that component.
	 * Checks if proposal is not available in older version.
	 */
	@Test
	public void testCamelCatalogVersionFeature() {
		insertComponent(COMPONENT);

		ContentAssistant assistant = sourceEditor.openContentAssistant();
		collector.checkThat(assistant.getProposals().contains(COMPONENT_PROPOSAL), equalTo(true));

		setCamelCatalogVersion(OLDER_VERSION);
		reopenEditor(CAMEL_CONTEXT);

		insertComponent(COMPONENT);
		assistant = sourceEditor.openContentAssistant();
		collector.checkThat(assistant.getProposals().contains(COMPONENT_PROPOSAL), equalTo(false));
	}

	/**
	 * Open file in editor.
	 *
	 * @param path to file.
	 */
	public void openFile(String... path) {
		ProjectItem item = new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem(path);
		item.open();
	}

	/**
	 * Inserts component to camel-context to uri="$HERE".
	 *
	 * @param component to be added represented by string.
	 */
	public void insertComponent(String component) {
		sourceEditor = new SourceEditor();
		cursorPosition = sourceEditor.getText().indexOf("uri");
		sourceEditor.setCursorPosition(cursorPosition + 5); // to write between ""
		sourceEditor.insertText(component);
		sourceEditor.setCursorPosition(cursorPosition + COMPONENT.length());
	}

	/**
	 * Changes used Camel Catalog Version. It's necessary to reopen editor to take
	 * effect.
	 *
	 * @param version of Camel Catalog represented by string.
	 */
	public void setCamelCatalogVersion(String version) {
		WorkbenchPreferenceDialog prefs = new WorkbenchPreferenceDialog();
		CamelCatalogVersion catalogVersion = new CamelCatalogVersion(prefs);
		prefs.open();
		prefs.select(catalogVersion);
		catalogVersion.setVersion(version);
		new PushButton("Apply").click();
		Shell shell = new DefaultShell("Preferences");
		new PushButton(shell, "Apply and Close").click();
	}

	/**
	 * Reopen editor to take effect of changes.
	 *
	 * @param file Name of file to be open after reopen.
	 */
	public void reopenEditor(String file) {
		sourceEditor.close();
		openFile(file);
		sourceEditor = new SourceEditor();
	}
}
