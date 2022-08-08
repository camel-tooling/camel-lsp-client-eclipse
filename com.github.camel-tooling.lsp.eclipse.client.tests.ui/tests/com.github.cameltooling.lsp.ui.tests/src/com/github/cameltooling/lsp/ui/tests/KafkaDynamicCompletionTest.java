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

import com.github.cameltooling.lsp.reddeer.editor.SourceEditor;
import com.github.cameltooling.lsp.reddeer.preference.KafkaConnectionURL;
import com.github.cameltooling.lsp.reddeer.utils.CreateNewEmptyFile;
import com.github.cameltooling.lsp.reddeer.utils.JavaProjectFactory;
import com.github.cameltooling.lsp.ui.tests.utils.EditorManipulator;

import org.eclipse.reddeer.common.properties.RedDeerProperties;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
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

/**
 *
 * @author fpospisi
 */
@RunWith(RedDeerSuite.class)
public class KafkaDynamicCompletionTest {
	public static final String PROJECT_NAME = "catalog-feature-test";
	public static final String CAMEL_CONTEXT = "camel-context.xml";
	public static final String RESOURCES_CONTEXT_PATH = "resources/kafka-dynamic-completion-context.xml";

	/*
	 *  Must be different from localhost:9092 which is default value and LSP client
	 *  tries to connect by default.
	 */
	public static final String KAFKA_CONNECTION_URL = "localhost:9093";

	// EXPECTED_TOPIC must be existing one, UNEXPECTED_TOPIC can't exist.
	public static final String COMPONENT = "kafka:";
	public static final String EXPECTED_TOPIC = "quickstart-events";
	public static final String UNEXPECTED_TOPIC = "random-events";

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

	/*
	 * Checks, if proposal for Kafka topic is not available. Then adds Kafka
	 * connection URL into properties and checks again. This time proposal should be
	 * available.
	 */
	@Test
	public void testKafkaDynamicCompletion() {
		insertComponent(COMPONENT);

		ContentAssistant assistant = sourceEditor.openContentAssistant();
		collector.checkThat(assistant.getProposals().contains(EXPECTED_TOPIC), equalTo(false));
		collector.checkThat(assistant.getProposals().contains(UNEXPECTED_TOPIC), equalTo(false));

		setKafkaConnectionURL(KAFKA_CONNECTION_URL);
		reopenEditor(CAMEL_CONTEXT);

		insertComponent(COMPONENT);
		assistant = sourceEditor.openContentAssistant();
		collector.checkThat(assistant.getProposals().contains(EXPECTED_TOPIC), equalTo(true));
		collector.checkThat(assistant.getProposals().contains(UNEXPECTED_TOPIC), equalTo(false));
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
		sourceEditor.setCursorPosition(cursorPosition + 5 + component.length());
	}

	/**
	 * Changes Kafka connection URL.
	 *
	 * @param URL of running Kafka connection represented by string.
	 */
	public void setKafkaConnectionURL(String URL) {
		WorkbenchPreferenceDialog prefs = new WorkbenchPreferenceDialog();
		KafkaConnectionURL connectionURL = new KafkaConnectionURL(prefs);
		prefs.open();
		prefs.select(connectionURL);
		connectionURL.setURL(URL);
		new PushButton(prefs, "Apply and Close").click();
	}

	/**
	 * Reopen editor to take effect of changes.
	 *
	 * @param file Name of file to be open after reopen.
	 */
	public void reopenEditor(String file) {
		sourceEditor.close();
		ProjectItem item = new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem(file);
		item.open();
		sourceEditor = new SourceEditor();
	}
}
