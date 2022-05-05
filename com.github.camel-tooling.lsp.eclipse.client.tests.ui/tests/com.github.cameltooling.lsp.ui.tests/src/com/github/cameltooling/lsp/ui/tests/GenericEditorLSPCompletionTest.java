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
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

import com.github.cameltooling.lsp.ui.tests.utils.EditorManipulator;
import com.github.cameltooling.lsp.reddeer.preference.DefaultEditorPreferencePage;
import com.github.cameltooling.lsp.reddeer.utils.JavaProjectFactory;
import com.github.cameltooling.lsp.reddeer.utils.CreateNewEmptyFile;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * Tests <i>Apache Camel Tooling LSP client</i> in Generic Text Editor</br>
 *
 * @author djelinek
 */
@RunWith(RedDeerSuite.class)
public class GenericEditorLSPCompletionTest extends DefaultLSPCompletionTest {

	@BeforeClass
	public static void prepareEnvironment() {
		WorkbenchPreferenceDialog prefs = new WorkbenchPreferenceDialog();
		DefaultEditorPreferencePage defaultEditor = new DefaultEditorPreferencePage(prefs);
		prefs.open();
		prefs.select(defaultEditor);
		defaultEditor.set(".xml", "Generic Text Editor");
		prefs.ok();
		
		JavaProjectFactory.create(PROJECT_NAME);
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		CreateNewEmptyFile.XMLFile(CAMEL_CONTEXT);
		new DefaultEditor(CAMEL_CONTEXT).activate();
		EditorManipulator.copyFileContentToXMLEditor(RESOURCES_CONTEXT_PATH);
	}
	
	// ... execute UI tests from DefaultLSPCompletionTest class

}
