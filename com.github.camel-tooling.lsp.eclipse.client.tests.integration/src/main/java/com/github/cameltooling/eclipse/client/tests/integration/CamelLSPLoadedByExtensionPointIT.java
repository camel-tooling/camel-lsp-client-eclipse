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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CamelLSPLoadedByExtensionPointIT {
	
	private static final int ARBITRARY_NUMBER_OF_MINIMAL_CAMEL_COMPONENTS = 200;
	private ICompletionProposal[] proposals;
	private TestSetupHelper testSetupHelper;
	
	@Before
	public void setup() {
		proposals = null;
	}
	
	@After
	public void tearDown() throws CoreException {
		testSetupHelper.clean();
	}

	@Test
	public void testGenericEditorProvideCompletion() throws Exception {
		String camelXmlFileContent = "<from uri=\"\" xmlns=\"http://camel.apache.org/schema/spring\"></from>\n";
		testSetupHelper = new TestSetupHelper();
		ITextViewer textViewer = testSetupHelper.createFileInProjectAndOpenInEditor(CamelLSPLoadedByExtensionPointIT.class.getSimpleName(), camelXmlFileContent);
		
		new DisplayHelper() {
			
			LSContentAssistProcessor lsContentAssistProcessor = new LSContentAssistProcessor();

			@Override
			protected boolean condition() {
				proposals = lsContentAssistProcessor.computeCompletionProposals(textViewer, 11);
				return proposals.length > ARBITRARY_NUMBER_OF_MINIMAL_CAMEL_COMPONENTS;
			}
		}.waitForCondition(Display.getDefault(), 3000);
		
		checkContainsATimerProposal(proposals);
	}

	private void checkContainsATimerProposal(ICompletionProposal[] proposals) {
		assertThat(Stream.of(proposals)
			.map(ICompletionProposal::getDisplayString)
			.filter(displayString -> displayString.contains("timer"))
			.findFirst().get()).isNotNull();
	}
}
