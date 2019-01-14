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

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.JavaProjectWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardFirstPage;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;

import com.github.cameltooling.lsp.reddeer.editor.SourceEditor;
import com.github.cameltooling.lsp.reddeer.utils.LogChecker;
import com.github.cameltooling.lsp.reddeer.wizard.NewXMLFileFirstPage;
import com.github.cameltooling.lsp.reddeer.wizard.NewXMLFileWizard;
import com.github.cameltooling.lsp.ui.tests.utils.EditorManipulator;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

/**
 * Tests <i>Apache Camel Tooling LSP client</i> in XML Editor (Source tab)</br>
 * <b>Quick tests:</b>
 * <ul>
 * 	<li>testComponentSchemes</li>
 * 	<li>testEndpointOptions</li>
 * 	<li>testAdditionalEndpointOptions</li>
 * 	<li>testDuplicateOptionsFilltering</li>
 * </ul>
 *
 * @author djelinek
 */
@RunWith(RedDeerSuite.class)
public class CamelLSPCompletionTest extends DefaultTest {

	public static final String PROJECT_NAME = "lsp";
	public static final String CAMEL_CONTEXT = "camel-context.xml";
	public static final String INSERT_SPACE = " ";
	public static final String RESOURCES_CONTEXT_PATH = "resources/camel-context-cbr.xml";
	public static final String EDITOR_SOURCE_TAB = "Source";

	private SourceEditor editor;
	private ContentAssistant assistant;
	private int cursorPosition;

	@BeforeClass
	public static void prepareEnvironment() {
		createNewJavaProject(PROJECT_NAME);
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		createNewEmptyXMLFile(CAMEL_CONTEXT);
		new DefaultEditor(CAMEL_CONTEXT).activate();;
		new DefaultCTabItem(EDITOR_SOURCE_TAB).activate();
		EditorManipulator.copyFileContentToXMLEditor(RESOURCES_CONTEXT_PATH);
	}

	@After
	public void cleanEditor() {
		editor.activate();
		EditorManipulator.copyFileContentToXMLEditor(RESOURCES_CONTEXT_PATH);
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	/**
	 * Tests code completion is working for component schemes (the part before the ":")
	 */
	@Test
	public void testComponentSchemes() {
		editor = new SourceEditor();

		cursorPosition = editor.getText().indexOf("<from");
		editor.setCursorPosition(cursorPosition + 24);
		assertComponentSchemes(editor.getCompletionProposals());

		cursorPosition = editor.getText().indexOf("<to");
		editor.setCursorPosition(cursorPosition + 20);
		assertComponentSchemes(editor.getCompletionProposals());

		LogChecker.assertNoLSPError();
	}

	/**
	 * Tests code completion is working for endpoint options (the part after the "?")
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

		LogChecker.assertNoLSPError();
	}

	/**
	 * Tests code completion is working for additional endpoint options (the part after "&")
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

		LogChecker.assertNoLSPError();
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

		LogChecker.assertNoLSPError();
	}

	/**
	 * Create new empty XML file
	 */
	private static void createNewEmptyXMLFile(String fileName) {
		NewXMLFileWizard newWizXML = new NewXMLFileWizard();
		newWizXML.open();
		NewXMLFileFirstPage newWizXMLPage = new NewXMLFileFirstPage(newWizXML);
		newWizXMLPage.setFileName(fileName);
		newWizXML.finish();
	}

	/**
	 * Prepare testing project
	 */
	private static void createNewJavaProject(String name) {
		JavaProjectWizard javaWiz = new JavaProjectWizard();
		javaWiz.open();
		JavaProjectWizardFirstPage javaWizPage = new JavaProjectWizardFirstPage(javaWiz);
		javaWizPage.setName(name);
		javaWiz.finish(TimePeriod.DEFAULT);
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
