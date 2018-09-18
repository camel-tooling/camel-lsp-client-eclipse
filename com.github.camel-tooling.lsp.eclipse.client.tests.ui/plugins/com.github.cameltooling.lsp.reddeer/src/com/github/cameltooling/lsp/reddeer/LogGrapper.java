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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.eclipse.ui.views.log.LogMessage;
import org.eclipse.reddeer.eclipse.ui.views.log.LogView;

/**
 * Utilizes access to Error Log View
 * 
 * @author tsedmik
 */
public class LogGrapper {

	/**
	 * Retrieves all error logs about given plugin
	 * 
	 * @param plugin name of plugin or substring of it
	 * @return List of errors in the plugin. In case of no error occurred, an empty
	 *         List is returned
	 */
	public static List<LogMessage> getPluginErrors(String plugin) {

		List<LogMessage> errors = new ArrayList<LogMessage>();
		LogView log = new LogView();
		log.open();
		List<LogMessage> allErrors = log.getErrorMessages();
		for (LogMessage message : allErrors) {
			if (message.getPlugin().toLowerCase().contains(plugin)) {
				errors.add(message);
			}
		}
		return errors;
	}
}