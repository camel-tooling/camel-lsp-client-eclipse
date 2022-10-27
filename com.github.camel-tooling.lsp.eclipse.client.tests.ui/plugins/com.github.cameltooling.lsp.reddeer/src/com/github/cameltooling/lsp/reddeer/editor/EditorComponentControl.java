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

package com.github.cameltooling.lsp.reddeer.editor;

/**
 * Handles most common actions required while working with Components and LSP.
 * 
 * @author fpospisi
 */
public class EditorComponentControl {

	/**
	 * Inserts component to camel-context to place="$HERE".
	 * 
	 * @param component Component to be added represented by string.
	 * @param place     Place inside camel-context.
	 */
	public static void insertComponent(String component, String place) {
		SourceEditor sourceEditor = new SourceEditor();
		int cursorPosition = sourceEditor.getText().indexOf(place) + place.length() + 2;
		sourceEditor.setCursorPosition(cursorPosition); // to write between ""
		sourceEditor.insertText(component);
		sourceEditor.setCursorPosition(cursorPosition + component.length()); 
		
	}

	/**
	 * Removes component from opened camel-context.
	 * 
	 * @param component Component to be removed form camel-context.
	 */
	public static void removeComponent(String component) {
		SourceEditor sourceEditor = new SourceEditor();
		String withoutComponent = sourceEditor.getText().replace(component, "");
		sourceEditor.selectText(0, withoutComponent.length() + component.length());
		sourceEditor.insertText(withoutComponent);
	}
}