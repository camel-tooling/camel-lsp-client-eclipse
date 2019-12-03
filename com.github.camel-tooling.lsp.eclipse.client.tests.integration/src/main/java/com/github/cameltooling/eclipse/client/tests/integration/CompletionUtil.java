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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.tests.util.DisplayHelper;
import org.eclipse.lsp4e.operations.completion.LSContentAssistProcessor;
import org.eclipse.swt.widgets.Display;

public class CompletionUtil {
	
	ICompletionProposal[] proposals = null;

	public void checkAdditionalCompletion(ITextViewer textViewer, long numberOfExpectedTextCompletion, String expectedTextCompletion, int position) {
		proposals = null;
		
		new DisplayHelper() {

			LSContentAssistProcessor lsContentAssistProcessor = new LSContentAssistProcessor();

			@Override
			protected boolean condition() {
				proposals = lsContentAssistProcessor.computeCompletionProposals(textViewer, position);
				if(retrieveDisplayStringOfProposals(proposals)
						.anyMatch(displayString -> displayString.contains("Computing proposals"))) {
					System.out.println("Computing proposals...");
					return false;
				}
				System.out.println("test condition");
				System.out.println("Current proposals: "+ retrieveDisplayStringOfProposals(proposals).collect(Collectors.joining(";")));
				return count(proposals, expectedTextCompletion) == numberOfExpectedTextCompletion;
			}

			
		}.waitForCondition(Display.getDefault(), 10000);
		
		assertThat(count(proposals, expectedTextCompletion))
			.as("Current proposals: "+ retrieveDisplayStringOfProposals(proposals).collect(Collectors.joining(";")))
			.isEqualTo(numberOfExpectedTextCompletion);
	}
	
	private long count(ICompletionProposal[] proposals, String expectedTextCompletion) {
		return retrieveDisplayStringOfProposals(proposals)
				.filter(displayString -> displayString.contains(expectedTextCompletion)).count();
	}
	
	private Stream<String> retrieveDisplayStringOfProposals(ICompletionProposal[] proposals) {
		return Stream.of(proposals).map(ICompletionProposal::getDisplayString);
	}
	
}
