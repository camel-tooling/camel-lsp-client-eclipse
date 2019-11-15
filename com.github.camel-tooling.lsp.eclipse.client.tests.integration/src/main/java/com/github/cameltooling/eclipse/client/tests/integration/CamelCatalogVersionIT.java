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

public class CamelCatalogVersionIT {

	private static final String A_CAMEL_CATALOG_VERSION_WITHOUT_JGROUPSRAFT_COMPONENT = "2.22.0";
	private ICompletionProposal[] proposals;
	private TestSetupHelper testSetupHelper;

	@Before
	public void setup() {
		proposals = null;
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}
	
	@After
	public void tearDown() throws BackingStoreException, CoreException {
		new CamelLanguageServerPreferenceManager().setCamelCatalogVersion("");
		testSetupHelper.clean();
	}

	@Test
	public void testUseCamelCatalogVersionPreference() throws Exception {
		String camelXmlFileContent = "<from uri=\"jgroups-raft\" xmlns=\"http://camel.apache.org/schema/spring\"></from>\n";
		testSetupHelper = new TestSetupHelper();
		ITextViewer textViewer = testSetupHelper.createFileInProjectAndOpenInEditor(CamelCatalogVersionIT.class.getSimpleName(), camelXmlFileContent);
		checkJGroupRaftCompletion(textViewer, 1);
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
		new CamelLanguageServerPreferenceManager().setCamelCatalogVersion(A_CAMEL_CATALOG_VERSION_WITHOUT_JGROUPSRAFT_COMPONENT);
		textViewer = testSetupHelper.openGenericEditor();
		
		checkJGroupRaftCompletion(textViewer, 0);
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
		new CamelLanguageServerPreferenceManager().setCamelCatalogVersion("");
		textViewer = testSetupHelper.openGenericEditor();
		
		checkJGroupRaftCompletion(textViewer, 1);
	}

	private void checkJGroupRaftCompletion(ITextViewer textViewer, long numberOfJgroupsRaftCompletion) {
		new DisplayHelper() {

			LSContentAssistProcessor lsContentAssistProcessor = new LSContentAssistProcessor();

			@Override
			protected boolean condition() {
				proposals = lsContentAssistProcessor.computeCompletionProposals(textViewer, 21);
				if(Stream.of(proposals).map(ICompletionProposal::getDisplayString)
						.anyMatch(displayString -> displayString.contains("Computing proposals"))) {
					return false;
				}
				return count() == numberOfJgroupsRaftCompletion;
			}

			long count() {
				return Stream.of(proposals).map(ICompletionProposal::getDisplayString)
						.filter(displayString -> displayString.contains("jgroups-raft:clusterName")).count();
			}
		}.waitForCondition(Display.getDefault(), 10000);
		
		long count = Stream.of(proposals).map(ICompletionProposal::getDisplayString)
						.filter(displayString -> displayString.contains("jgroups-raft:clusterName")).count();
		assertThat(count).isEqualTo(numberOfJgroupsRaftCompletion);
	}

}
