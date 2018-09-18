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

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.direct.preferences.Preferences;

public class ConsolePreferenceUtil {

	public static final Logger log = Logger.getLogger(ConsolePreferenceUtil.class);

	public static final String CONSOLE_PLUGIN = "org.eclipse.debug.ui";
	public static final String CONSOLE_OPEN_ON_ERR_KEY = "DEBUG.consoleOpenOnErr";
	public static final String CONSOLE_OPEN_ON_OUT_KEY = "DEBUG.consoleOpenOnOut";

	/**
	 * Decides whether the console opens on error
	 * 
	 * @return true if the console opens on error, false otherwise
	 */
	public static boolean isConsoleOpenOnError() {
		return "true".equalsIgnoreCase(Preferences.get(CONSOLE_PLUGIN, CONSOLE_OPEN_ON_ERR_KEY));
	}

	/**
	 * Decides whether the console opens on standard output
	 * 
	 * @return true if the console opens on standard output, false otherwise
	 */
	public static boolean isConsoleOpenOnOutput() {
		return "true".equalsIgnoreCase(Preferences.get(CONSOLE_PLUGIN, CONSOLE_OPEN_ON_OUT_KEY));
	}

	/**
	 * Sets the console open on standard output
	 */
	public static void setConsoleOpenOnError(boolean openOnError) {
		log.info("Sets the console open on error to '" + openOnError + "'");
		Preferences.set(CONSOLE_PLUGIN, CONSOLE_OPEN_ON_ERR_KEY, String.valueOf(openOnError));
	}

	/**
	 * Sets the console open on standard output
	 */
	public static void setConsoleOpenOnOutput(boolean openOnOutput) {
		log.info("Sets the console open on error to '" + openOnOutput + "'");
		Preferences.set(CONSOLE_PLUGIN, CONSOLE_OPEN_ON_OUT_KEY, String.valueOf(openOnOutput));
	}
}
