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

import com.github.cameltooling.lsp.reddeer.editor.EditorComponentControl;
import com.github.cameltooling.lsp.reddeer.editor.SourceEditor;
import com.github.cameltooling.lsp.reddeer.preference.CamelExtraComponents;
import com.github.cameltooling.lsp.reddeer.utils.CreateNewEmptyFile;
import com.github.cameltooling.lsp.reddeer.utils.JavaProjectFactory;
import com.github.cameltooling.lsp.ui.tests.utils.EditorManipulator;
import com.github.cameltooling.lsp.ui.tests.utils.TimeoutPeriodManipulator;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.log.LogView;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.cleanerrorlog.CleanErrorLogRequirement;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
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

/*
*
* @author fpospisi
*/
@RunWith(RedDeerSuite.class)
public class AdditionalComponentFeatureTest extends DefaultTest {

	public static final String PROJECT_NAME = "AdditionalComponentTest";
	public static final String CAMEL_CONTEXT = "camel-context.xml";
	public static final String SOURCE_TAB = "Source";
	public static final String COMPONENT_PLACE = "uri";

	private static final String COMPONENT_NAME = "abcd";
	private static final String COMPONENT_PROPOSAL = "abcd:xyz";
	public static final String RESOURCES_CONTEXT_PATH = "resources/additional-component-feature-context.xml";
	public static final String EXTRA_COMPONENT_JSON_PATH = "resources/additional-component-feature-component.json";

	private SourceEditor sourceEditor;

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
		TimeoutPeriodManipulator.setFactor(3);
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
		TimeoutPeriodManipulator.clearFactor();
		setAdditionalComponent(""); // set version back to empty
	}

	@After
	public void deleteProject() {
		JavaProjectFactory.deleteAllProjects();
		LogView log = new LogView();
		log.open();
		log.deleteLog();
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();

		new CleanWorkspaceRequirement().fulfill();
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	/**
	 * Inserts custom component into template. Then checks if proposal is available.
	 * Custom component is added into environment preferences and editor is
	 * reopened. Checks again if proposal is available.
	 */
	@Test
	public void testAdditionalComponentFeature() {
		EditorComponentControl.insertComponent(COMPONENT_NAME, COMPONENT_PLACE);
		collector.checkThat(testProposal(COMPONENT_NAME, COMPONENT_PROPOSAL), equalTo(false));
		setAdditionalComponent(EditorManipulator.getFileContent(EXTRA_COMPONENT_JSON_PATH));
		SourceEditor.reopenEditor(PROJECT_NAME, CAMEL_CONTEXT);
		EditorComponentControl.insertComponent(COMPONENT_NAME, COMPONENT_PLACE);
		collector.checkThat(testProposal(COMPONENT_NAME, COMPONENT_PROPOSAL), equalTo(true));
	}

	/*
	 * Inserts additional component in Apache Camel preference page.
	 * 
	 * @param component Representation of additional component in JSON valid format.
	 */
	public void setAdditionalComponent(String component) {
		WorkbenchPreferenceDialog prefs = new WorkbenchPreferenceDialog();
		CamelExtraComponents extraComponents = new CamelExtraComponents(prefs);
		prefs.open();
		prefs.select(extraComponents);
		extraComponents.setComponents(component);
		new PushButton("Apply").click();
		Shell shell = new DefaultShell("Preferences");
		new PushButton(shell, "Apply and Close").click();
	}

	/*
	 * Tests if expected proposal is available for component.
	 * 
	 * @param component Tested component.
	 * 
	 * @param expectedProposal Expected proposal for component.
	 * 
	 * @return true Proposal is available.
	 * 
	 * @return false Proposal is not available.
	 */
	public boolean testProposal(String component, String expectedProposal) {
		sourceEditor = new SourceEditor();
		int cursorPosition = sourceEditor.getText().indexOf(component);
		sourceEditor.setCursorPosition(cursorPosition + component.length());
		ContentAssistant assistant = sourceEditor.openContentAssistant();
		return assistant.getProposals().contains(expectedProposal);
	}
}