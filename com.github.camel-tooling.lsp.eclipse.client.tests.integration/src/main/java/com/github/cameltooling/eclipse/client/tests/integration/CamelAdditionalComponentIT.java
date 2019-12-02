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

import java.util.stream.Stream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.tests.util.DisplayHelper;
import org.eclipse.lsp4e.operations.completion.LSContentAssistProcessor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.prefs.BackingStoreException;

import com.github.cameltooling.eclipse.preferences.CamelLanguageServerPreferenceManager;

public class CamelAdditionalComponentIT {
	
	private ICompletionProposal[] proposals;
	private TestSetupHelper testSetupHelper;

	@Before
	public void setup() {
		proposals = null;
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
		checkAdditionalCompletion(textViewer, 0, expectedTextCompletion);
		
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
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
		new CamelLanguageServerPreferenceManager().setCamelAdditionalComponents(component);
		textViewer = testSetupHelper.openGenericEditor();
		
		checkAdditionalCompletion(textViewer, 1, expectedTextCompletion);
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
		new CamelLanguageServerPreferenceManager().setCamelAdditionalComponents(null);
		textViewer = testSetupHelper.openGenericEditor();
		
		checkAdditionalCompletion(textViewer, 0, expectedTextCompletion);
	}
	
	@Test
	public void testInvalidJSonIsIgnored() throws Exception {
		new CamelLanguageServerPreferenceManager().setCamelAdditionalComponents("invalid");
		String camelXmlFileContent = "<from uri=\"\" xmlns=\"http://camel.apache.org/schema/spring\"></from>\n";
		testSetupHelper = new TestSetupHelper();
		ITextViewer textViewer = testSetupHelper.createFileInProjectAndOpenInEditor(CamelAdditionalComponentIT.class.getSimpleName(), camelXmlFileContent);		
		checkAdditionalCompletion(textViewer, 1, "timer:timerName");
	}
	
	private void checkAdditionalCompletion(ITextViewer textViewer, long numberOfAdditionalCompletion, String expectedTextCompletion) {
		new DisplayHelper() {

			LSContentAssistProcessor lsContentAssistProcessor = new LSContentAssistProcessor();

			@Override
			protected boolean condition() {
				proposals = lsContentAssistProcessor.computeCompletionProposals(textViewer, 11);
				if(Stream.of(proposals).map(ICompletionProposal::getDisplayString)
						.anyMatch(displayString -> displayString.contains("Computing proposals"))) {
					return false;
				}
				return count() == numberOfAdditionalCompletion;
			}

			long count() {
				return Stream.of(proposals).map(ICompletionProposal::getDisplayString)
						.filter(displayString -> displayString.contains(expectedTextCompletion)).count();
			}
		}.waitForCondition(Display.getDefault(), 10000);
		
		long count = Stream.of(proposals).map(ICompletionProposal::getDisplayString)
						.filter(displayString -> displayString.contains(expectedTextCompletion)).count();
		assertThat(count).isEqualTo(numberOfAdditionalCompletion);
	}

}
