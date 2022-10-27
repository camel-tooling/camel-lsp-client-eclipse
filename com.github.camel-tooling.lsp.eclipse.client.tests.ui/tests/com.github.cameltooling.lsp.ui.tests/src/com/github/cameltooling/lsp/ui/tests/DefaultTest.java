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
package com.github.cameltooling.lsp.ui.tests;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.ui.views.log.LogView;
import org.eclipse.reddeer.requirements.cleanerrorlog.CleanErrorLogRequirement;
import org.eclipse.reddeer.requirements.cleanerrorlog.CleanErrorLogRequirement.CleanErrorLog;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import com.github.cameltooling.lsp.reddeer.preference.ConsolePreferenceUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Prepares environment for UI testing
 * 
 * @author djelinek
 */
@CleanWorkspace
@CleanErrorLog
public abstract class DefaultTest {

	private static Logger log = Logger.getLogger(DefaultTest.class);

	/**
	 * Prepares test environment
	 */
	@BeforeClass
	public static void defaultBeforeClassSetup() {
		log.info("Maximizing workbench shell.");
		new WorkbenchShell().maximize();

		log.info("Disable showing Console view after standard output changes");
		ConsolePreferenceUtil.setConsoleOpenOnError(false);
		ConsolePreferenceUtil.setConsoleOpenOnOutput(false);

		log.info("Disable showing Error Log view after changes");
		LogView logView = new LogView();
		logView.open();
		logView.setActivateOnNewEvents(false);
	}

	/**
	 * Prepares test environment
	 */
	@Before
	public void defaultBeforeTestSetup() {
		log.info("Deleting Error Log.");
		new CleanErrorLogRequirement().fulfill();
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void defaultAfterTestClean() {
		log.info("Closing all non-workbench shells.");
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	/**
	 * Cleans up test environment
	 */
	@AfterClass
	public static void defaultAfterClassClean() {
		log.info("Closing all non-workbench shells.");
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		log.info("Deleting all projects");
		new CleanWorkspaceRequirement().fulfill();
		log.info("Clean Error Log");
		new CleanErrorLogRequirement().fulfill();
	}

}