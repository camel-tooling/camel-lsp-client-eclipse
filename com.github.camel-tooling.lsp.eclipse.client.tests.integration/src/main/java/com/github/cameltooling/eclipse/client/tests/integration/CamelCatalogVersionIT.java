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

import org.assertj.core.api.Assertions;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.prefs.BackingStoreException;

import com.github.cameltooling.eclipse.preferences.CamelLanguageServerPreferenceManager;

public class CamelCatalogVersionIT {

	private static final String A_CAMEL_CATALOG_VERSION_WITHOUT_JGROUPSRAFT_COMPONENT = "2.22.0";
	private TestSetupHelper testSetupHelper;

	@Before
	public void setup() {
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
		
		assertThat(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false)).isTrue();
		new CamelLanguageServerPreferenceManager().setCamelCatalogVersion(A_CAMEL_CATALOG_VERSION_WITHOUT_JGROUPSRAFT_COMPONENT);
		textViewer = testSetupHelper.openGenericEditor();
		
		checkJGroupRaftCompletion(textViewer, 0);
		
		assertThat(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false)).isTrue();
		new CamelLanguageServerPreferenceManager().setCamelCatalogVersion("");
		textViewer = testSetupHelper.openGenericEditor();
		
		checkJGroupRaftCompletion(textViewer, 1);
	}

	private void checkJGroupRaftCompletion(ITextViewer textViewer, long numberOfJgroupsRaftCompletion) {
		new CompletionUtil().checkAdditionalCompletion(textViewer, numberOfJgroupsRaftCompletion, "jgroups-raft:clusterName", 21);
	}

}
