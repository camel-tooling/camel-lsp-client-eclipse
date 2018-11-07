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
package com.github.cameltooling.lsp.reddeer;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin {

	public static final String PLUGINID = "com.github.cameltooling.lsp.reddeer";
	private static Activator plugin;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		setInstance(this);
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		setInstance(null);
	}
	
	private static synchronized void setInstance(Activator plugin) {
		Activator.plugin = plugin;
	}
	
	public static synchronized Activator getInstance() {
		return plugin;
	}
}
