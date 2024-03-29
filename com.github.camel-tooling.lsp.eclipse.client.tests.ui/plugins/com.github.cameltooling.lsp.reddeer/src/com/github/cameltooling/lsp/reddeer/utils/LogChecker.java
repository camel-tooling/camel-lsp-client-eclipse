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
package com.github.cameltooling.lsp.reddeer.utils;

import static org.junit.Assert.assertTrue;

import com.github.cameltooling.lsp.reddeer.LogGrapper;

/**
 * Checks 'lsp' errors in Error Log View
 * 
 * @author djelinek
 */
public class LogChecker {
	
	private LogChecker() {
		//private constructor, only static access
	}
	
	public static boolean noLSPError() {
		return LogGrapper.getPluginErrors("lsp").isEmpty();
	}
	
	public static void assertNoLSPError() {
		assertTrue("Console contains 'Apache Camel LSP' errors", noLSPError());
	}
	
	public static boolean noCamelClientLSPError() {
		return LogGrapper.getPluginErrors("camel-tooling").isEmpty();
	}
	
	public static void assertNoCamelClientError() {
		assertTrue("Console contains 'Apache Camel Client LSP' errors", noCamelClientLSPError());
	}
	
}
