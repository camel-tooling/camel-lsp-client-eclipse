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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import com.github.cameltooling.eclipse.client.ActivatorCamelLspClient;

public class CamelLanguageServerPreferenceManager {

	private static final String TOP_NODE_CAMEL_KEY = "camel";
	static final String CAMEL_CATALOG_VERSION_PREF_KEY = "Camel catalog version";

	public Map<String, Map<String, String>> getPreferenceAsLanguageServerFormat() {
		IEclipsePreferences preferences = getCamelPreferenceNode();
		Map<String, Map<String, String>> settings = new HashMap<>();
		Map<String, String> camelSettings = new HashMap<>();
		camelSettings.put(CAMEL_CATALOG_VERSION_PREF_KEY, preferences.get(CAMEL_CATALOG_VERSION_PREF_KEY, null));
		settings.put(TOP_NODE_CAMEL_KEY, camelSettings );
		return settings;
	}
	
	public void setCamelCatalogVersion(String camelVersion) throws BackingStoreException {
		IEclipsePreferences preferences = getCamelPreferenceNode();
		if (camelVersion == null || camelVersion.isEmpty()) {
			preferences.remove(CAMEL_CATALOG_VERSION_PREF_KEY);
		} else {
			preferences.put(CAMEL_CATALOG_VERSION_PREF_KEY, camelVersion);
		}
		preferences.flush();
	}

	private IEclipsePreferences getCamelPreferenceNode() {
		return InstanceScope.INSTANCE.getNode(ActivatorCamelLspClient.ID);
	}
}
