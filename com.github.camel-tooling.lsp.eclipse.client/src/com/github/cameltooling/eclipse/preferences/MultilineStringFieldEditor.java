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
package com.github.cameltooling.eclipse.preferences;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MultilineStringFieldEditor extends StringFieldEditor {
	
	private int validateStrategyMultiline;
	private int textLimitMultiline;
	private Text textControlMultiline;

	public MultilineStringFieldEditor(String camelAdditionalComponentPrefKey, String camelAdditionalComponentSettings, Composite fieldEditorParent) {
		super(camelAdditionalComponentPrefKey, camelAdditionalComponentSettings, fieldEditorParent);
	}

	@Override
	public void setValidateStrategy(int value) {
		super.setValidateStrategy(value);
		validateStrategyMultiline = value;
	}
	
	@Override
	public void setTextLimit(int limit) {
		super.setTextLimit(limit);
		textLimitMultiline = limit;
	}
	
	@Override
	public Text getTextControl(Composite parent) {
		textControlMultiline = super.getTextControl();
		if (textControlMultiline == null) {
			textControlMultiline = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.LEFT | SWT.V_SCROLL);
			textControlMultiline.setFont(parent.getFont());
			switch (validateStrategyMultiline) {
			case VALIDATE_ON_KEY_STROKE:
				textControlMultiline.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(KeyEvent e) {
						valueChanged();
					}
				});
				textControlMultiline.addFocusListener(new FocusAdapter() {
					// Ensure that the value is checked on focus loss in case we
					// missed a keyRelease or user hasn't released key.
					// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=214716
					@Override
					public void focusLost(FocusEvent e) {
						valueChanged();
					}
				});


				break;
			case VALIDATE_ON_FOCUS_LOST:
				textControlMultiline.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						clearErrorMessage();
					}
				});
				textControlMultiline.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						refreshValidState();
					}

					@Override
					public void focusLost(FocusEvent e) {
						valueChanged();
					}
				});
				break;
			default:
				Assert.isTrue(false, "Unknown validate strategy");//$NON-NLS-1$
			}
			textControlMultiline.addDisposeListener(event -> textControlMultiline = null);
			if (textLimitMultiline > 0) {//Only set limits above 0 - see SWT spec
				textControlMultiline.setTextLimit(textLimitMultiline);
			}
		} else {
			checkParent(textControlMultiline, parent);
		}
		return textControlMultiline;
	}
	
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
	    super.doFillIntoGrid(parent, numColumns);

	    textControlMultiline = super.getTextControl();
	    GridData gd = (GridData) textControlMultiline.getLayoutData();
	    gd.verticalAlignment = GridData.FILL;
	    gd.grabExcessVerticalSpace = true;
	    textControlMultiline.setLayoutData(gd);

	    Label label = getLabelControl(parent);
	    gd = new GridData();
	    gd.verticalAlignment = SWT.TOP;
	    label.setLayoutData(gd);
	  }
	
}
