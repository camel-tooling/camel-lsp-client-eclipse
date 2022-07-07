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
package com.github.cameltooling.lsp.reddeer.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard dialog first page for creating new Java Class.
 * 
 * @author fpospisi
 */
public class NewJavaClassFirstPage extends WizardPage {

	public static final String NAME = "Name:";
	
	public NewJavaClassFirstPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public LabeledText getClassNameTXT() {
		return new LabeledText(this, NAME);
	}

	public String getClassName() {
		return new LabeledText(this, NAME).getText();
	}

	public void setClassName(String str) {
		new LabeledText(this, NAME).setText(str);
	}
	
}
