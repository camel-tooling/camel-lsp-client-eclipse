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

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;

/**
 * Represents 'Source' tab of Editor
 * 
 * @author tsedmik
 */
public class SourceEditor extends DefaultEditor {

	private DefaultStyledText editor = new DefaultStyledText();

	/**
	 * Sets position of the cursor to the specified position
	 * 
	 * @param position
	 *                     position in the editor
	 */
	public void setCursorPosition(int position) {
		editor.selectPosition(position);
	}

	/**
	 * Inserts given text on the cursor position
	 * 
	 * @param text
	 *                 text to be inserted
	 */
	public void insertText(String text) {
		editor.insertText(text);
	}

	/**
	 * Returns text in the editor
	 * 
	 * @return text in the editor
	 */
	public String getText() {
		return editor.getText();
	}

	public int getPosition(String text) {
		return editor.getPositionOfText(text);
	}

	public void selectText(int from, int to) {
		editor.setSelection(from, to);
	}

	/**
	 * Opens ContentAssistant and return available code completion proposals
	 * 
	 * @return List<String>
	 */
	public List<String> getCompletionProposals() {
		ContentAssistant assistant = openContentAssistant();
		List<String> proposals = assistant.getProposals();
		assistant.close();
		return proposals;
	}
	
	@Override
	public void activate() {
		EditorHandler.getInstance().activate(editorPart);
	}

    public void save() {
        log.debug("Saving editor");
        Display.syncExec(new Runnable() {

            @Override
            public void run() {
                editorPart.doSave(new NullProgressMonitor());
            }
        });
    }
    
    public boolean isDirty() {
        return Display.syncExec(new ResultRunnable<Boolean>() {

            @Override
            public Boolean run() {
                return editorPart.isDirty();
            }
        });
    }
	
}
