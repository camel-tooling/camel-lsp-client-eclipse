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

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.github.cameltooling.eclipse.client.ActivatorCamelLspClient;
import com.github.cameltooling.eclipse.internal.l10n.Messages;

public class CamelLanguageServerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, ActivatorCamelLspClient.ID));
		setDescription(Messages.camelPreferencePageDescription);
	}

	@Override
	protected void createFieldEditors() {
		Composite fieldEditorParent = getFieldEditorParent();
		fieldEditorParent.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		addField(new StringFieldEditor(CamelLanguageServerPreferenceManager.CAMEL_CATALOG_VERSION_PREF_KEY, Messages.camelCatalogVersionSettings, fieldEditorParent));
		addField(new MultilineStringFieldEditor(CamelLanguageServerPreferenceManager.CAMEL_ADDITIONAL_COMPONENT_PREF_KEY, Messages.camelAdditionalComponentSettings, fieldEditorParent));
	}
}
