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

import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;

public class FileTypeDialog {

	public static final String FILE_TYPE = "File type:";
	public static final String ADD_FILE_TYPE = "Add File Type";

	// Generated class methods (5)
	public LabeledText getFileTypeTXT() {
		return new LabeledText(FILE_TYPE);
	}

	public String getTextFileType() {
		return new LabeledText(FILE_TYPE).getText();
	}

	public DefaultShell getShellAddFileType() {
		return new DefaultShell(ADD_FILE_TYPE);
	}

	public String getTextAddFileType() {
		return new DefaultShell(ADD_FILE_TYPE).getText();
	}

	public void setTextFileType(String str) {
		new LabeledText(FILE_TYPE).setText(str);
	}

}
