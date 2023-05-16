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

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.log.LogView;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.cleanerrorlog.CleanErrorLogRequirement;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Arrays;
import java.util.Collection;

import com.github.cameltooling.lsp.reddeer.editor.EditorComponentControl;
import com.github.cameltooling.lsp.reddeer.editor.SourceEditor;
import com.github.cameltooling.lsp.reddeer.preference.CamelRuntimeProvider;
import com.github.cameltooling.lsp.reddeer.utils.CreateNewEmptyFile;
import com.github.cameltooling.lsp.reddeer.utils.JavaProjectFactory;
import com.github.cameltooling.lsp.ui.tests.utils.EditorManipulator;
import com.github.cameltooling.lsp.ui.tests.utils.TimeoutPeriodManipulator;

/*
*
* @author fpospisi
*/
@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class CamelRuntimeProviderFeatureTest extends DefaultTest {

	public static final String PROJECT_NAME = "CatalogFeatureTest";
	public static final String CAMEL_CONTEXT = "camel-context.xml";
	public static final String RESOURCES_CONTEXT_PATH = "resources/catalog-version-feature-context.xml";
	public static final String SOURCE_TAB = "Source";
	public static final String COMPONENT_PLACE = "uri";

	public static final String DEFAULT_PROVIDER = "Default";
	public static final String SB_PROVIDER = "Spring Boot";
	public static final String QUARKUS_PROVIDER = "Quarkus";
	public static final String KARAF_PROVIDER = "Karaf";

	public static final String KNATIVE = "knative";
	public static final String MONGO = "mongo";
	public static final String JMX = "jmx";

	public static final String KNATIVE_PROP = "knative:type/typeId";
	public static final String MONGO_PROP = "mongodb:connectionBean";
	public static final String JMX_PROP = "jmx:serverURL";

	private SourceEditor sourceEditor;

	@Parameter
	public String runtime;

	@Parameter(1)
	public boolean knative_av;

	@Parameter(2)
	public boolean mongo_av;

	@Parameter(3)
	public boolean jmx_av;

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				// runtime provider, knative available, mongo available, jmx available
				{ "Spring Boot", true, true, true }, 
				{ "Quarkus", true, true, false }, //knative is available in latest quarkus catalog
				{ "Karaf", false, true, true } 
				});
	}

	/**
	 * Changes runtime provider. Sets Creates empty project, then creates XML file
	 * with camel-context.
	 */
	@Before
	public void setupTestEnvironment() {
		setCamelRuntimeProvider(runtime);
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
		TimeoutPeriodManipulator.setFactor(5);
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

		// Set runtime provider back to Default.
		setCamelRuntimeProvider(DEFAULT_PROVIDER);
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
	 * Checks if component proposal is available for currently tested runtime. If
	 * component is part of tested runtime, proposal should be available.
	 */
	@Test
	public void testCamelRuntimeProvider() {
		EditorComponentControl.insertComponent(KNATIVE, COMPONENT_PLACE);
		sourceEditor = new SourceEditor();
		ContentAssistant assistant = sourceEditor.openContentAssistant();
		collector.checkThat(assistant.getProposals().contains(KNATIVE_PROP), equalTo(knative_av));
		//AbstractWait.sleep(TimePeriod.getCustom(1000));
		EditorComponentControl.removeComponent(KNATIVE);

		EditorComponentControl.insertComponent(MONGO, COMPONENT_PLACE);
		sourceEditor = new SourceEditor();
		assistant = sourceEditor.openContentAssistant();
		collector.checkThat(assistant.getProposals().contains(MONGO_PROP), equalTo(mongo_av));
		EditorComponentControl.removeComponent(MONGO);

		EditorComponentControl.insertComponent(JMX, COMPONENT_PLACE);
		sourceEditor = new SourceEditor();
		assistant = sourceEditor.openContentAssistant();
		collector.checkThat(assistant.getProposals().contains(JMX_PROP), equalTo(jmx_av));
		EditorComponentControl.removeComponent(JMX);
	}

	/**
	 * Changes used Camel runtime provider. It's necessary to reopen editor to take
	 * effect.
	 *
	 * @param provider of Camel runtime represented by string.
	 */
	public void setCamelRuntimeProvider(String provider) {
		WorkbenchPreferenceDialog prefs = new WorkbenchPreferenceDialog();
		CamelRuntimeProvider runtimeProvider = new CamelRuntimeProvider(prefs);
		prefs.open();
		prefs.select(runtimeProvider);

		runtimeProvider.setProvider(provider);

		new PushButton(prefs, "Apply and Close").click();
	}
}