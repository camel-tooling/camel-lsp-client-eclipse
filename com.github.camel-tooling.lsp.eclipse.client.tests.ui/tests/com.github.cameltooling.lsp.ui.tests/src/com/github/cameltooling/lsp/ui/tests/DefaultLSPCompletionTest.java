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

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.views.log.LogView;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.requirements.cleanerrorlog.CleanErrorLogRequirement;
import org.eclipse.reddeer.requirements.cleanerrorlog.CleanErrorLogRequirement.CleanErrorLog;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;

import com.github.cameltooling.lsp.reddeer.editor.SourceEditor;
import com.github.cameltooling.lsp.reddeer.preference.ConsolePreferenceUtil;
import com.github.cameltooling.lsp.reddeer.utils.LogChecker;
import com.github.cameltooling.lsp.ui.tests.utils.EditorManipulator;
import com.github.cameltooling.lsp.ui.tests.utils.TimeoutPeriodManipulator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

/**
 * Setup and run <i>Apache Camel Tooling LSP client</i> UI tests</br>
 * <b>Quick tests:</b>
 * <ul>
 * <li>testComponentSchemes</li>
 * <li>testEndpointOptions</li>
 * <li>testAdditionalEndpointOptions</li>
 * <li>testDuplicateOptionsFilltering</li>
 * </ul>
 *
 * @author djelinek
 */
@CleanWorkspace
@CleanErrorLog
public abstract class DefaultLSPCompletionTest {

	private static Logger log = Logger.getLogger(DefaultLSPCompletionTest.class);

	public static final String RESOURCES_CONTEXT_PATH = "resources/camel-context-cbr.xml";
	public static final String PROJECT_NAME = "lsp";
	public static final String CAMEL_CONTEXT = "camel-context.xml";
	public static final String INSERT_SPACE = " ";
	public static final String EDITOR_SOURCE_TAB = "Source";

	private SourceEditor editor;
	private ContentAssistant assistant;
	private int cursorPosition;

	/**
	 * Prepares test environment
	 */
	@BeforeClass
	public static void defaultBeforeClassSetup() {
		log.info("Maximizing workbench shell.");
		new WorkbenchShell().maximize();

		log.info("Disable showing Console view after standard output changes");
		ConsolePreferenceUtil.setConsoleOpenOnError(false);
		ConsolePreferenceUtil.setConsoleOpenOnOutput(false);

		log.info("Disable showing Error Log view after changes");
		LogView logView = new LogView();
		logView.open();
		logView.setActivateOnNewEvents(false);
	}

	/**
	 * Prepares test environment
	 */
	@Before
	public void defaultBeforeTestSetup() {
		log.info("Deleting Error Log.");
		new CleanErrorLogRequirement().fulfill();
	}

	@Before
	public void setupTimeout() {
		TimeoutPeriodManipulator.setFactor(5);
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void defaultAfterTestClean() {
		log.info("Closing all non-workbench shells.");
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	@After
	public void tearDown() {
		editor.activate();
		EditorManipulator.copyFileContentToXMLEditor(RESOURCES_CONTEXT_PATH);
		TimeoutPeriodManipulator.clearFactor();
	}

	/**
	 * Cleans up test environment
	 */
	@AfterClass
	public static void defaultAfterClassClean() {
		log.info("Closing all non-workbench shells.");
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		log.info("Deleting all projects");
		new CleanWorkspaceRequirement().fulfill();
		log.info("Clean Error Log");
		new CleanErrorLogRequirement().fulfill();
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	/**
	 * Tests code completion is working for component schemes (the part before the
	 * ":")
	 */
	@Test
	public void testComponentSchemes() {
		//AbstractWait.sleep(TimePeriod.getCustom(1100));
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("<from");
		editor.setCursorPosition(cursorPosition + 24);
		assertComponentSchemes(editor.getCompletionProposals());

		cursorPosition = editor.getText().indexOf("<to");
		editor.setCursorPosition(cursorPosition + 20);
		assertComponentSchemes(editor.getCompletionProposals());

		LogChecker.assertNoCamelClientError();
	}

	/**
	 * Tests code completion is working for endpoint options (the part after the
	 * "?")
	 */
	@Test
	public void testEndpointOptions() {
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("<from");
		editor.setCursorPosition(cursorPosition += 42);
		tryEndpointOptionsCompletion();

		cursorPosition = editor.getText().indexOf("<to");
		editor.setCursorPosition(cursorPosition += 42);
		tryEndpointOptionsCompletion();

		LogChecker.assertNoCamelClientError();
	}

	/**
	 * Tests code completion is working for additional endpoint options (the part
	 * after "&")
	 */
	@Test
	public void testAdditionalEndpointOptions() {
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("<from");
		editor.setCursorPosition(cursorPosition += 42);
		tryAdditionalOptionsCompletion();

		cursorPosition = editor.getText().indexOf("<to");
		editor.setCursorPosition(cursorPosition += 42);
		tryAdditionalOptionsCompletion();

		LogChecker.assertNoCamelClientError();
	}

	/**
	 * Tests duplicate endpoint options are filtered out
	 */
	@Test
	public void testDuplicateOptionsFiltering() {
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("<from");
		editor.setCursorPosition(cursorPosition += 42);
		tryOptionsFiltering();

		cursorPosition = editor.getText().indexOf("<to");
		editor.setCursorPosition(cursorPosition += 42);
		tryOptionsFiltering();

		LogChecker.assertNoCamelClientError();
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
		editor.insertText("?fileName=testFileName&amp;");
		editor.setCursorPosition(cursorPosition += 27);
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
		editor.insertText("&amp;e");
		editor.setCursorPosition(cursorPosition += 27);
		assistant = editor.openContentAssistant();
		collector.checkThat(assistant.getProposals().isEmpty(), equalTo(false));
		collector.checkThat(assistant.getProposals().contains("exchangePattern"), equalTo(false));
		assistant.close();

		new WaitWhile(new ShellIsAvailable(assistant), TimePeriod.MEDIUM);
		editor.save();
	}
}
