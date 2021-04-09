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
package com.github.cameltooling.eclipse.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.osgi.framework.Bundle;

import com.github.cameltooling.eclipse.preferences.CamelLanguageServerPreferenceManager;

public class CamelLSPStreamConnectionProvider extends ProcessStreamConnectionProvider {

	private static final String DEBUG_FLAG = "debugLSPServer";

	public CamelLSPStreamConnectionProvider() {
		super(computeCommands(), computeWorkingDir());
	}

	private static String computeWorkingDir() {
		return System.getProperty("user.dir");
	}

	private static List<String> computeCommands() {
		List<String> commands = new ArrayList<>();
		commands.add("java");
		if (isDebugEnabled())
			commands.addAll(debugArguments());
		commands.add("-jar");
		commands.add(computeCamelLanguageServerJarPath());
		return commands;
	}

	private static String computeCamelLanguageServerJarPath() {
		String camelLanguageServerJarPath = "";
		Bundle bundle = Platform.getBundle(ActivatorCamelLspClient.ID);
		URL fileURL = bundle.findEntries("/libs", "camel-lsp-server-*.jar", false).nextElement();
		try {
			URL resolvedUrl = FileLocator.resolve(fileURL);
			File file = new File(URIUtil.toURI(resolvedUrl));
			if (Platform.OS_WIN32.equals(Platform.getOS())) {
				camelLanguageServerJarPath = "\"" + file.getAbsolutePath() + "\"";
			} else {
				camelLanguageServerJarPath = file.getAbsolutePath();
			}
		} catch (URISyntaxException | IOException exception) {
			ActivatorCamelLspClient.getInstance().getLog().log(new Status(IStatus.ERROR, ActivatorCamelLspClient.ID,
					"Cannot get the Camel LSP Server jar.", exception)); //$NON-NLS-1$
		}
		return camelLanguageServerJarPath;
	}

	private static boolean isDebugEnabled() {
		return Boolean.parseBoolean(System.getProperty(DEBUG_FLAG, "false"));
	}

	private static List<String> debugArguments() {
		return Arrays.asList("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=3000");
	}

	@Override
	public Object getInitializationOptions(URI rootUri) {
		return new CamelLanguageServerPreferenceManager().getPreferenceAsLanguageServerFormat();
	}

}
