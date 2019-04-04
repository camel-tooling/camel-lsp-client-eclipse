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
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.tests.util.DisplayHelper;
import org.eclipse.lsp4e.operations.completion.LSContentAssistProcessor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.genericeditor.ExtensionBasedTextEditor;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.Before;
import org.junit.Test;

public class CamelLSPLoadedByExtensionPointIT {
	
	private static final int ARBITRARY_NUMBER_OF_MINIMAL_CAMEL_COMPONENTS = 200;
	private ICompletionProposal[] proposals;
	
	@Before
	public void setup() {
		proposals = null;
	}

	@Test
	public void testGenericEditorProvideCompletion() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(CamelLSPLoadedByExtensionPointIT.class.getSimpleName());
		project.create(null);
		project.open(null);
		IFile camelFile = project.getFile("camelFile.xml");
		camelFile.create(new ByteArrayInputStream("<from uri=\"\" xmlns=\"http://camel.apache.org/schema/spring\"></from>\n".getBytes()), IResource.FORCE, null);
		IEditorPart openEditor = IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), camelFile, "org.eclipse.ui.genericeditor.GenericEditor");
		assertThat(openEditor).isInstanceOf(ExtensionBasedTextEditor.class);
		
		ITextViewer textViewer = getTextViewer(openEditor);
		
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
	
	/**
	 * Retrieve the TextViewer by reflection on a protected method as it is not publicly available.
	 * it is used also by LSP4E Tests org.eclipse.lsp4e.test.TestUtils.getTextViewer(IEditorPart)
	 * Unfortunately, the code  cannot be reused as it is in a fragment.
	 */
	public ITextViewer getTextViewer(IEditorPart part) throws InvocationTargetException {
		try {			
			if (part instanceof ITextEditor) {
				ITextEditor textEditor = (ITextEditor) part;

				Method getSourceViewerMethod = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer"); //$NON-NLS-1$
				getSourceViewerMethod.setAccessible(true);
				return (ITextViewer) getSourceViewerMethod.invoke(textEditor);
			} else {
				fail("Unable to open editor");
				return null;
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new InvocationTargetException(e);
		}
	}

}
