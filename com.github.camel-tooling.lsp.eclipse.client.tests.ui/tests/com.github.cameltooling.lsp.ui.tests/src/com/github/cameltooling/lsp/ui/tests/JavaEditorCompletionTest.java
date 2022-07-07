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

import org.eclipse.reddeer.common.properties.RedDeerProperties;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.log.LogView;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;

import com.github.cameltooling.lsp.reddeer.editor.SourceEditor;
import com.github.cameltooling.lsp.reddeer.preference.ConsolePreferenceUtil;
import com.github.cameltooling.lsp.reddeer.utils.CreateNewEmptyFile;
import com.github.cameltooling.lsp.reddeer.utils.JavaProjectFactory;
import com.github.cameltooling.lsp.ui.tests.utils.EditorManipulator;

/*
*
* @author fpospisi
*/
@RunWith(RedDeerSuite.class)
public class JavaEditorCompletionTest {

	private static final String TIMEOUT_PERIOD_FACTOR_PROPETY_NAME = RedDeerProperties.TIME_PERIOD_FACTOR.getName();
	private String timePeriodfactor;

	public static final String PROJECT_NAME = "java-dsl-completion-test";
	public static final String CAMEL_ROUTE_NAME = "CamelRoute";
	public static final String CAMEL_ROUTE = "CamelRoute.java";

	public static final String RESOURCES_ROUTE_PATH = "resources/java-editor-completion-test-route.java";

	public static final String INPUT = "file";
	public static final String EXPECTED_PROPOSAL = "file:directoryName";

	private SourceEditor editor;
	private ContentAssistant assistant;
	private int cursorPosition;

	/*
	 * Default setup.
	 */
	@BeforeClass
	public static void defaultBeforeClassSetup() {
		new WorkbenchShell().maximize();

		ConsolePreferenceUtil.setConsoleOpenOnError(false);
		ConsolePreferenceUtil.setConsoleOpenOnOutput(false);

		LogView logView = new LogView();
		logView.open();
		logView.setActivateOnNewEvents(false);
	}

	/**
	 * Creates empty project, then creates Java file with camel-context.
	 */
	@BeforeClass
	public static void setupTestEnvironment() {
		JavaProjectFactory.create(PROJECT_NAME);
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		CreateNewEmptyFile.JavaClass(CAMEL_ROUTE_NAME);
		EditorManipulator.copyFileContent(RESOURCES_ROUTE_PATH);
	}

	@Before
	public void setupTimeout() {
		timePeriodfactor = System.getProperty(TIMEOUT_PERIOD_FACTOR_PROPETY_NAME);
		System.setProperty(TIMEOUT_PERIOD_FACTOR_PROPETY_NAME, "5");
		TimePeriod.updateFactor();
	}

	/*
	 * Restores file content to default before next test.
	 */
	@After
	public void restoreFileContent() {
		EditorManipulator.copyFileContent(RESOURCES_ROUTE_PATH);
	}

	/**
	 * Cleans up test environment.
	 */
	@After
	public void tearDown() {
		if (timePeriodfactor != null) {
			System.setProperty(TIMEOUT_PERIOD_FACTOR_PROPETY_NAME, timePeriodfactor);
		} else {
			System.clearProperty(TIMEOUT_PERIOD_FACTOR_PROPETY_NAME);
		}
		TimePeriod.updateFactor();
	}

	@AfterClass
	public static void deleteProject() {
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
	 * Tests code completion is working for component schemes (the part before the
	 * ":")
	 */
	@Test
	public void testComponentSchemes() {
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("from");
		editor.setCursorPosition(cursorPosition + 10);
		assertComponentSchemes(editor.getCompletionProposals());

		cursorPosition = editor.getText().indexOf("to");
		editor.setCursorPosition(cursorPosition + 8);
		assertComponentSchemes(editor.getCompletionProposals());
	}

	/**
	 * Tests code completion is working for endpoint options (the part after the
	 * "?")
	 */
	@Test
	public void testEndpointOptions() {
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("from");
		editor.setCursorPosition(cursorPosition += 25);
		tryEndpointOptionsCompletion();

		cursorPosition = editor.getText().indexOf("to");
		editor.setCursorPosition(cursorPosition += 27);
		tryEndpointOptionsCompletion();
	}

	/**
	 * Tests code completion is working for additional endpoint options (the part
	 * after "&")
	 */
	@Test
	public void testAdditionalEndpointOptions() {
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("from");
		editor.setCursorPosition(cursorPosition += 25);
		tryAdditionalOptionsCompletion();

		cursorPosition = editor.getText().indexOf("to");
		editor.setCursorPosition(cursorPosition += 27);
		tryAdditionalOptionsCompletion();
	}

	/**
	 * Tests duplicate endpoint options are filtered out
	 */
	@Test
	public void testDuplicateOptionsFiltering() {
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("from");
		editor.setCursorPosition(cursorPosition += 25);
		tryOptionsFiltering();

		cursorPosition = editor.getText().indexOf("to");
		editor.setCursorPosition(cursorPosition += 27);
		tryOptionsFiltering();
	}

	private void assertComponentSchemes(List<String> proposals) {
		collector.checkThat("Content assistant is empty", proposals.isEmpty(), equalTo(false));
		collector.checkThat("Content assistant is not filtered", proposals.get(0).startsWith("f"), equalTo(true));
	}

	private void tryEndpointOptionsCompletion() {
		editor.insertText("?e");
		editor.setCursorPosition(cursorPosition += 2);
		assistant = editor.openContentAssistant();

		collector.checkThat(assistant.getProposals().isEmpty(), equalTo(false));
		assistant.chooseProposal("exchangePattern");
		assistant = editor.openContentAssistant();

		collector.checkThat(assistant.getProposals().isEmpty(), equalTo(false));
		assistant.chooseProposal("InOnly");

		new WaitWhile(new ShellIsAvailable(assistant), TimePeriod.MEDIUM);
		editor.save();
	}

	private void tryAdditionalOptionsCompletion() {
		editor.insertText("?fileName=testFileName&");
		editor.setCursorPosition(cursorPosition += 23);
		assistant = editor.openContentAssistant();

		collector.checkThat(assistant.getProposals().isEmpty(), equalTo(false));
		assistant.chooseProposal("allowNullBody");

		new WaitWhile(new ShellIsAvailable(assistant), TimePeriod.MEDIUM);
		editor.save();
	}

	private void tryOptionsFiltering() {
		editor.insertText("?e");
		editor.setCursorPosition(cursorPosition += 2);
		assistant = editor.openContentAssistant();

		collector.checkThat(assistant.getProposals().isEmpty(), equalTo(false));
		assistant.chooseProposal("exchangePattern");
		assistant = editor.openContentAssistant();

		collector.checkThat(assistant.getProposals().isEmpty(), equalTo(false));
		assistant.chooseProposal("InOnly");
		editor.insertText("&");
		editor.setCursorPosition(cursorPosition += 22);
		assistant = editor.openContentAssistant();

		collector.checkThat(assistant.getProposals().isEmpty(), equalTo(false));
		collector.checkThat(assistant.getProposals().contains("exchangePattern"), equalTo(false));
		assistant.close();

		new WaitWhile(new ShellIsAvailable(assistant), TimePeriod.MEDIUM);
		editor.save();
	}
}
