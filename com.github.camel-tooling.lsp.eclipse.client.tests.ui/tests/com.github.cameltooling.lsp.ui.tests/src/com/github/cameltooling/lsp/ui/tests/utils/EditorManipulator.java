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
package com.github.cameltooling.lsp.ui.tests.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Scanner;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.matcher.WithTooltipTextMatcher;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import com.github.cameltooling.lsp.reddeer.ResourceHelper;
import com.github.cameltooling.lsp.reddeer.XPathEvaluator;
import com.github.cameltooling.lsp.ui.tests.Activator;

/**
 * Support static methods manipulates with Text Editor
 *
 * @author tsedmik
 */
public class EditorManipulator {

	private static Logger log = Logger.getLogger(EditorManipulator.class);

	/**
	 * Replaces content of a file opened in active text editor with content of the
	 * file <i>source</i>
	 *
	 * @param source Path to the source file
	 */
	public static void copyFileContent(String source) {
		TextEditor editor = new TextEditor();
		editor.setText(getFileContent(source));
		editor.save();
	}

	/**
	 * Replaces content of a file opened in active XML editor with content of the
	 * file <i>source</i>
	 *
	 * @param source Path to the source file
	 */
	public static void copyFileContentToXMLEditor(String source) {
		new DefaultStyledText().setText(EditorManipulator.getFileContent(source));
		new DefaultToolItem(new WorkbenchShell(), 0, new WithTooltipTextMatcher(new RegexMatcher("Save.*"))).click();
		try {
			log.debug("Check whether 'Could not parse your changes to the XML' dialog is appeared");
			new WaitUntil(new ShellIsAvailable("Could not parse your changes to the XML"), TimePeriod.SHORT);
			new DefaultShell("Could not parse your changes to the XML");
			new PushButton("OK").click();
		} catch (Exception e) {
			log.debug("Dialog 'Could not parse your changes to the XML' didn't appeared");
		}
	}

	/**
	 * Gets content of a given file
	 *
	 * @param source the source file
	 * @return content of the file, in case of some error - empty string
	 */
	public static String getFileContent(String source) {
		File testFile = new File(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, source));
		String text = "";
		try (Scanner scanner = new Scanner(testFile)) {
			scanner.useDelimiter("\\Z");
			text = scanner.next();
		} catch (FileNotFoundException e) {
			log.error("Resource missing: can't find a failing test case to copy (" + source + ")!");
		}
		log.info("Text in active text editor was replaced with content of the file: " + source + ".");
		return text;
	}

	/**
	 * Compares content of the active XML editor with content of the given file
	 *
	 * @param file path to the file
	 * @return true - content of the file and the text editor is the same, false -
	 *         otherwise
	 */
	public static boolean isEditorContentEqualsFile(String file) {
		String editorText = new DefaultStyledText().getText();
		if (file.equals("resources/camel-context-all.xml")) {
			XPathEvaluator xpath = new XPathEvaluator(new StringReader(editorText));
			if ((xpath.evaluateBoolean("/beans/camelContext/route/*[1]/@uri = 'file:src/data?noop=true'"))
					&& (xpath.evaluateString("/beans/camelContext/route/choice/*[1]/*[1][text()]")
							.equals("/person/city = 'London'"))
					&& (xpath.evaluateBoolean("/beans/camelContext/route/choice/*[1]/*[2]/@message = 'UK message'"))
					&& (xpath.evaluateBoolean(
							"/beans/camelContext/route/choice/*[1]/*[3]/@uri = 'file:target/messages/uk'"))
					&& (xpath.evaluateBoolean("/beans/camelContext/route/choice/*[2]/*[1]/@message = 'Other message'"))
					&& (xpath.evaluateBoolean(
							"/beans/camelContext/route/choice/*[2]/*[2]/@uri = 'file:target/messages/others'")))
				return true;
			return false;
		} else {
			String fileText = getFileContent(file);
			return editorText.equals(fileText);
		}
	}
}
