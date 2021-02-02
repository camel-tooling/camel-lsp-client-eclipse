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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import com.github.cameltooling.eclipse.client.ActivatorCamelLspClient;
import com.google.gson.Gson;

public class CamelLanguageServerPreferenceManager {

	private static final String TOP_NODE_CAMEL_KEY = "camel";
	static final String CAMEL_CATALOG_VERSION_PREF_KEY = "Camel catalog version";
	static final String CAMEL_ADDITIONAL_COMPONENT_PREF_KEY = "extra-components";
	static final String CAMEL_CATALOG_RUNTIME_PROVIDER_PREF_KEY = "Camel catalog runtime provider";

	public Map<String, Map<String, Object>> getPreferenceAsLanguageServerFormat() {
		IEclipsePreferences preferences = getCamelPreferenceNode();
		Map<String, Map<String, Object>> settings = new HashMap<>();
		Map<String, Object> camelSettings = new HashMap<>();
		camelSettings.put(CAMEL_CATALOG_VERSION_PREF_KEY, preferences.get(CAMEL_CATALOG_VERSION_PREF_KEY, null));
		camelSettings.put(CAMEL_CATALOG_RUNTIME_PROVIDER_PREF_KEY, preferences.get(CAMEL_CATALOG_RUNTIME_PROVIDER_PREF_KEY, null));
		camelSettings.put(CAMEL_ADDITIONAL_COMPONENT_PREF_KEY, getAdditionalComponentIfValid(preferences));
		settings.put(TOP_NODE_CAMEL_KEY, camelSettings );
		return settings;
	}

	private List<?> getAdditionalComponentIfValid(IEclipsePreferences preferences) {
		String additionalComponentAsString = preferences.get(CAMEL_ADDITIONAL_COMPONENT_PREF_KEY, null);
		if(additionalComponentAsString != null && !additionalComponentAsString.isEmpty()) {
			try {
				return new Gson().fromJson(additionalComponentAsString, List.class);
			} catch (Exception ex) {
				ActivatorCamelLspClient.getInstance().getLog().log(new Status(IStatus.ERROR, ActivatorCamelLspClient.ID, "Invalid JSON provided for extra Camel components.", ex));
				return Collections.emptyList();
			}
		}
		return Collections.emptyList();
	}
	
	public void setCamelCatalogVersion(String camelVersion) throws BackingStoreException {
		setPreferenceValue(camelVersion, CAMEL_CATALOG_VERSION_PREF_KEY);
	}

	private void setPreferenceValue(String camelVersion, String key) throws BackingStoreException {
		IEclipsePreferences preferences = getCamelPreferenceNode();
		if (camelVersion == null || camelVersion.isEmpty()) {
			preferences.remove(key);
		} else {
			preferences.put(key, camelVersion);
		}
		preferences.flush();
	}
	
	public void setCamelAdditionalComponents(String additionalComponents) throws BackingStoreException {
		setPreferenceValue(additionalComponents, CAMEL_ADDITIONAL_COMPONENT_PREF_KEY);
	}

	private IEclipsePreferences getCamelPreferenceNode() {
		return InstanceScope.INSTANCE.getNode(ActivatorCamelLspClient.ID);
	}

	public void setRuntimeProvider(String runtimeProvider) throws BackingStoreException {
		setPreferenceValue(runtimeProvider, CAMEL_CATALOG_RUNTIME_PROVIDER_PREF_KEY);
	}
}
