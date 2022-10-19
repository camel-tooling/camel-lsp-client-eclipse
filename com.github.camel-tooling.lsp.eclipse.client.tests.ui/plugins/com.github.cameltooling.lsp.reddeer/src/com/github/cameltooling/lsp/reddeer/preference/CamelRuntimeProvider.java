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
package com.github.cameltooling.lsp.reddeer.preference;

import java.util.List;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;

/**
 * Represents <i>Preferences... -> Apache Camel</i> page.
 *
 * @author fpospisi
 */
public class CamelRuntimeProvider extends PreferencePage {

	public CamelRuntimeProvider(ReferencedComposite ref) {
		super(ref, "Apache Camel");
	}

	/**
	 * Gets list of available runtime providers.
	 * 
	 * @return available runtime providers.
	 */
	public List<String> getProviders() {
		return new LabeledCombo("Runtime provider").getItems();
	}
	
	/**
	 * Gets current runtime provider.
	 * 
	 * @return current runtime provider.
	 */
	public String getCurrentProvider() {
		return new LabeledCombo("Runtime provider").getText();
	}

	/**
	 * Sets runtime provider.
	 * 
	 * @param required runtime provider.
	 */
	public void setProvider(String provider) {
		new LabeledCombo("Runtime provider").setSelection(provider);
	}
}
