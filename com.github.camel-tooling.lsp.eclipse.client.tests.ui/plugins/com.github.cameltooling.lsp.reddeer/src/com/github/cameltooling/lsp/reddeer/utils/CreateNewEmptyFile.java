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

import com.github.cameltooling.lsp.reddeer.wizard.NewJavaClassFirstPage;
import com.github.cameltooling.lsp.reddeer.wizard.NewJavaClassWizard;
import com.github.cameltooling.lsp.reddeer.wizard.NewXMLFileFirstPage;
import com.github.cameltooling.lsp.reddeer.wizard.NewXMLFileWizard;

/**
 * Creates new empty file.
 *
 * @author fpospisi
 */
public class CreateNewEmptyFile {

	/**
	 * Creates new empty XML file.
	 *
	 * @param filename Name of file.
	 */
	public static void XMLFile(String filename) {
		NewXMLFileWizard newWizXML = new NewXMLFileWizard();
		newWizXML.open();
		NewXMLFileFirstPage newWizXMLPage = new NewXMLFileFirstPage(newWizXML);
		newWizXMLPage.setFileName(filename);
		newWizXML.finish();
	}
	
	/**
	 * Creates new empty Java class.
	 *
	 * @param filename Name of class.
	 */
	public static void JavaClass(String filename) {
		NewJavaClassWizard newWizJavaClass = new NewJavaClassWizard();
		newWizJavaClass.open();
		NewJavaClassFirstPage newWizJavaClassPage = new NewJavaClassFirstPage(newWizJavaClass);
		newWizJavaClassPage.setClassName(filename);
		newWizJavaClass.finish();
	}
}
