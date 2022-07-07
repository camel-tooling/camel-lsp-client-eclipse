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
package com.github.cameltooling.lsp.ui.tests.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.github.cameltooling.lsp.ui.tests.AdditionalComponentFeatureTest;
import com.github.cameltooling.lsp.ui.tests.CamelCatalogVersionFeatureTest;
import com.github.cameltooling.lsp.ui.tests.CamelLSPCompletionTest;
import com.github.cameltooling.lsp.ui.tests.GenericEditorLSPCompletionTest;
import com.github.cameltooling.lsp.ui.tests.JavaEditorCompletionTest;
import com.github.cameltooling.lsp.ui.tests.PluginInstalledTest;

import junit.framework.TestSuite;

/**
 * Runs smoke tests on Camel LSP Client
 * 
 * @author djelinek
 */
@SuiteClasses({
	AdditionalComponentFeatureTest.class,
	CamelCatalogVersionFeatureTest.class,
	CamelLSPCompletionTest.class,
	GenericEditorLSPCompletionTest.class,
	JavaEditorCompletionTest.class,
	PluginInstalledTest.class
	})

@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
