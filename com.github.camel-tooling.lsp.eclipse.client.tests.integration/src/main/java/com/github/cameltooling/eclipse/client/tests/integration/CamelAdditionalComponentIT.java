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
package com.github.cameltooling.eclipse.client.tests.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.prefs.BackingStoreException;

import com.github.cameltooling.eclipse.preferences.CamelLanguageServerPreferenceManager;

public class CamelAdditionalComponentIT {
	
	private TestSetupHelper testSetupHelper;
	private CompletionUtil completionUtil = new CompletionUtil();

	@Before
	public void setup() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}
	
	@After
	public void tearDown() throws BackingStoreException, CoreException {
		new CamelLanguageServerPreferenceManager().setCamelAdditionalComponents(null);
		testSetupHelper.clean();
	}
	
	@Test
	public void testProvideExtraComponentPreference() throws Exception {
		String camelXmlFileContent = "<from uri=\"\" xmlns=\"http://camel.apache.org/schema/spring\"></from>\n";
		testSetupHelper = new TestSetupHelper();
		ITextViewer textViewer = testSetupHelper.createFileInProjectAndOpenInEditor(CamelAdditionalComponentIT.class.getSimpleName(), camelXmlFileContent);
		String expectedTextCompletion = "acomponent:withsyntax";
		completionUtil.checkAdditionalCompletion(textViewer, 0, expectedTextCompletion, 11);
		
		String component = "[{\n" + 
				" \"component\": {\n" + 
				"    \"kind\": \"component\",\n" + 
				"    \"scheme\": \"acomponent\",\n" + 
				"    \"syntax\": \"acomponent:withsyntax\",\n" + 
				"    \"title\": \"A Component\",\n" + 
				"    \"description\": \"Description of my component.\",\n" + 
				"    \"label\": \"\",\n" + 
				"    \"deprecated\": false,\n" + 
				"    \"deprecationNote\": \"\",\n" + 
				"    \"async\": false,\n" + 
				"    \"consumerOnly\": true,\n" + 
				"    \"producerOnly\": false,\n" + 
				"    \"lenientProperties\": false,\n" + 
				"    \"javaType\": \"org.test.AComponent\",\n" + 
				"    \"firstVersion\": \"1.0.0\",\n" + 
				"    \"groupId\": \"org.test\",\n" + 
				"    \"artifactId\": \"camel-acomponent\",\n" + 
				"    \"version\": \"3.0.0-RC3\"\n" + 
				"  },\n" + 
				"  \"componentProperties\": {\n" + 
				"  },\n" + 
				"  \"properties\": {\n" + 
				"  }\n" + 
				"}]";
		
		assertThat(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false)).isTrue();
		new CamelLanguageServerPreferenceManager().setCamelAdditionalComponents(component);
		textViewer = testSetupHelper.openGenericEditor();
		
		completionUtil.checkAdditionalCompletion(textViewer, 1, expectedTextCompletion, 11);
		
		assertThat(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false)).isTrue();
		new CamelLanguageServerPreferenceManager().setCamelAdditionalComponents(null);
		textViewer = testSetupHelper.openGenericEditor();
		
		completionUtil.checkAdditionalCompletion(textViewer, 0, expectedTextCompletion, 11);
	}
	
	@Test
	public void testInvalidJSonIsIgnored() throws Exception {
		new CamelLanguageServerPreferenceManager().setCamelAdditionalComponents("invalid");
		String camelXmlFileContent = "<from uri=\"\" xmlns=\"http://camel.apache.org/schema/spring\"></from>\n";
		testSetupHelper = new TestSetupHelper();
		ITextViewer textViewer = testSetupHelper.createFileInProjectAndOpenInEditor(CamelAdditionalComponentIT.class.getSimpleName(), camelXmlFileContent);		
		completionUtil.checkAdditionalCompletion(textViewer, 1, "timer:timerName", 11);
	}
	
}
