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

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import java.util.List;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;

public class DefaultEditorPreferencePage extends PreferencePage {

	public static final String OPEN_UNASSOCIATED_FILES_WITH_ASK_VIA_POP_UP = "Ask via pop-up";

	public static final String OPEN_UNASSOCIATED_FILES_WITH_SEARCH_MARKETPLACE = "Search Marketplace";

	public static final String OPEN_UNASSOCIATED_FILES_WITH_SYSTEM_EDITOR_IF_NONE_TEXT_EDITOR = "System Editor; if none: Text Editor";

	public static final String OPEN_UNASSOCIATED_FILES_WITH_TEXT_EDITOR = "Text Editor";
	
	public DefaultEditorPreferencePage(ReferencedComposite ref) {
		super(ref, "General", "Editors", "File Associations");
	}
	
	/**
	 * Sets default editor for required file type. 
	 *
	 * @param type File type for change.
	 * @param editor Required editor.
	 */
	public void set(String type, String editor) {
		if (type == null || editor == null) {
			return;
		}
		
		// .foo and *.foo are equal in the end
		String filetype;
		if (type.substring(0, 1).equals(".")) {
			filetype = "*" + type;
		} else {
			filetype = type;
		}

		// if file type not available, add new file type
		if (!fileTypeIsAvailable(filetype)) {
			this.clickAddBTN();
			new FileTypeDialog().setTextFileType(filetype);
			new OkButton().click();
		}

		// if editor is available, set as default
		if (editorIsAvailable(editor)) {
			List<TableItem> availableEditors = new DefaultTable(1).getItems();
			for (TableItem availableEditor : availableEditors) {
				if (availableEditor.getText().startsWith(editor)) {
					availableEditor.select();
				}
			}
			this.clickDefaultBTN();
		} else {
			return;
		}
	}

	/**
	 * Checks if file type is already inside list.
	 * 
	 * @param filetype Required file type. 
	 * @return true - file type is already in list
	 * @return false - otherwise
	 */
	public boolean fileTypeIsAvailable(String filetype) {
		List<TableItem> jreItems = new DefaultTable().getItems();
		for (TableItem jreItem : jreItems) {
			System.out.println("ft: " + jreItem.getText());
			if (jreItem.getText().equals(filetype)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if editor is available in list.
	 * 
	 * @param editor Required editor.
	 * @return true - editor is available
	 * @return false - otherwise
	 */
	public boolean editorIsAvailable(String editor) {
		List<TableItem> availableEditors = new DefaultTable(1).getItems();
		for (TableItem availableEditor : availableEditors) {
			System.out.println("ae: " + availableEditor.getText());
			if (availableEditor.getText().startsWith(editor)) {
				return true;
			}
		}
		return false;
	}

	// Generated class methods (74)
	public void clickApplyAndCloseBTN() {
		new PushButton("Apply and Close").click();
	}

	public void clickDefaultBTN() {
		new PushButton("Default").click();
	}

	public void clickRemoveBTN() {
		new PushButton("Remove").click();
	}

	public void clickAddBTN() {
		new PushButton("Add...").click();
	}

	public void clickApplyBTN() {
		new PushButton("Apply").click();
	}

	public void clickRestoreDefaultsBTN() {
		new PushButton("Restore Defaults").click();
	}

	public PushButton getApplyAndCloseBTN() {
		return new PushButton("Apply and Close");
	}

	public String getTextApplyAndClose() {
		return new PushButton("Apply and Close").getText();
	}

	public PushButton getDefaultBTN() {
		return new PushButton("Default");
	}

	public String getTextDefault() {
		return new PushButton("Default").getText();
	}

	public PushButton getRemoveBTN() {
		return new PushButton("Remove");
	}

	public String getTextRemove() {
		return new PushButton("Remove").getText();
	}

	public PushButton getAddBTN() {
		return new PushButton("Add...");
	}

	public String getTextAdd() {
		return new PushButton("Add...").getText();
	}

	public LabeledCombo getOpenUnassociatedFilesWithCMB() {
		return new LabeledCombo("Open unassociated files with:");
	}

	public String getTextOpenUnassociatedFilesWith() {
		return new LabeledCombo("Open unassociated files with:").getText();
	}

	public String getSelectionOpenUnassociatedFilesWith() {
		return new LabeledCombo("Open unassociated files with:").getSelection();
	}

	public List<String> getItemsOpenUnassociatedFilesWith() {
		return new LabeledCombo("Open unassociated files with:").getItems();
	}

	public PushButton getApplyBTN() {
		return new PushButton("Apply");
	}

	public String getTextApply() {
		return new PushButton("Apply").getText();
	}

	public PushButton getRestoreDefaultsBTN() {
		return new PushButton("Restore Defaults");
	}

	public String getTextRestoreDefaults() {
		return new PushButton("Restore Defaults").getText();
	}

	public LabeledText getNumberOfOpenedEditorsBeforeClosingTXT() {
		return new LabeledText("Number of opened editors before closing:");
	}

	public String getTextNumberOfOpenedEditorsBeforeClosing() {
		return new LabeledText("Number of opened editors before closing:").getText();
	}

	public CheckBox getCloseEditorsAutomaticallyCHB() {
		return new CheckBox("Close editors automatically");
	}

	public String getTextCloseEditorsAutomatically() {
		return new CheckBox("Close editors automatically").getText();
	}

	public CheckBox getPromptToSaveOnCloseEvenIfStillOpenElsewhereCHB() {
		return new CheckBox("Prompt to save on close even if still open elsewhere");
	}

	public String getTextPromptToSaveOnCloseEvenIfStillOpenElsewhere() {
		return new CheckBox("Prompt to save on close even if still open elsewhere").getText();
	}

	public CheckBox getRestoreEditorStateOnStartupCHB() {
		return new CheckBox("Restore editor state on startup");
	}

	public String getTextRestoreEditorStateOnStartup() {
		return new CheckBox("Restore editor state on startup").getText();
	}

	public CheckBox getAllowInplaceSystemEditorsCHB() {
		return new CheckBox("Allow in-place system editors");
	}

	public String getTextAllowInplaceSystemEditors() {
		return new CheckBox("Allow in-place system editors").getText();
	}

	public LabeledText getSizeOfRecentlyOpenedFilesListTXT() {
		return new LabeledText("Size of recently opened files list:");
	}

	public String getTextSizeOfRecentlyOpenedFilesList() {
		return new LabeledText("Size of recently opened files list:").getText();
	}

	public CheckBox getOpenWhenUsingArrowKeysCHBgroup() {
		return new CheckBox(new DefaultGroup("Open mode"),"Open when using arrow keys");
	}

	public String getTextOpenWhenUsingArrowKeys() {
		return new CheckBox(new DefaultGroup("Open mode"),"Open when using arrow keys").getText();
	}

	public CheckBox getSelectOnHoverCHBgroup() {
		return new CheckBox(new DefaultGroup("Open mode"),"Select on hover");
	}

	public String getTextSelectOnHover() {
		return new CheckBox(new DefaultGroup("Open mode"),"Select on hover").getText();
	}

	public RadioButton getSingleClickRDBgroup() {
		return new RadioButton(new DefaultGroup("Open mode"),"Single click");
	}

	public String getTextSingleClick() {
		return new RadioButton(new DefaultGroup("Open mode"),"Single click").getText();
	}

	public RadioButton getDoubleClickRDBgroup() {
		return new RadioButton(new DefaultGroup("Open mode"),"Double click");
	}

	public String getTextDoubleClick() {
		return new RadioButton(new DefaultGroup("Open mode"),"Double click").getText();
	}

	public LabeledText getWorkbenchSaveIntervalInMinutesTXT() {
		return new LabeledText("Workbench save interval (in minutes):");
	}

	public String getTextWorkbenchSaveIntervalInMinutes() {
		return new LabeledText("Workbench save interval (in minutes):").getText();
	}

	public CheckBox getRenameResourceInlineIfAvailableCHB() {
		return new CheckBox("Rename resource inline if available");
	}

	public String getTextRenameResourceInlineIfAvailable() {
		return new CheckBox("Rename resource inline if available").getText();
	}

	public CheckBox getShowHeapStatusCHB() {
		return new CheckBox("Show heap status");
	}

	public String getTextShowHeapStatus() {
		return new CheckBox("Show heap status").getText();
	}

	public DefaultShell getShellPreferences() {
		return new DefaultShell("Preferences");
	}

	public String getTextPreferences() {
		return new DefaultShell("Preferences").getText();
	}

	public boolean isCheckedCloseEditorsAutomaticallyCHB() {
		return new CheckBox("Close editors automatically").isChecked();
	}

	public boolean isCheckedPromptToSaveOnCloseEvenIfStillOpenElsewhereCHB() {
		return new CheckBox("Prompt to save on close even if still open elsewhere").isChecked();
	}

	public boolean isCheckedRestoreEditorStateOnStartupCHB() {
		return new CheckBox("Restore editor state on startup").isChecked();
	}

	public boolean isCheckedAllowInplaceSystemEditorsCHB() {
		return new CheckBox("Allow in-place system editors").isChecked();
	}

	public boolean isCheckedOpenWhenUsingArrowKeysGroup() {
		return new CheckBox(new DefaultGroup("Open mode"),"Open when using arrow keys").isChecked();
	}

	public boolean isCheckedSelectOnHoverGroup() {
		return new CheckBox(new DefaultGroup("Open mode"),"Select on hover").isChecked();
	}

	public boolean isSelectedSingleClickGroup() {
		return new RadioButton(new DefaultGroup("Open mode"),"Single click").isSelected();
	}

	public boolean isSelectedDoubleClickGroup() {
		return new RadioButton(new DefaultGroup("Open mode"),"Double click").isSelected();
	}

	public boolean isCheckedRenameResourceInlineIfAvailableCHB() {
		return new CheckBox("Rename resource inline if available").isChecked();
	}

	public boolean isCheckedShowHeapStatusCHB() {
		return new CheckBox("Show heap status").isChecked();
	}

	public void setSelectionOpenUnassociatedFilesWith(String str) {
		new LabeledCombo("Open unassociated files with:").setSelection(str);
	}

	public void setTextNumberOfOpenedEditorsBeforeClosing(String str) {
		new LabeledText("Number of opened editors before closing:").setText(str);
	}

	public void setTextSizeOfRecentlyOpenedFilesList(String str) {
		new LabeledText("Size of recently opened files list:").setText(str);
	}

	public void setTextWorkbenchSaveIntervalInMinutes(String str) {
		new LabeledText("Workbench save interval (in minutes):").setText(str);
	}

	public void toggleCloseEditorsAutomaticallyCHB(boolean choice) {
		new CheckBox("Close editors automatically").toggle(choice);
	}

	public void togglePromptToSaveOnCloseEvenIfStillOpenElsewhereCHB(boolean choice) {
		new CheckBox("Prompt to save on close even if still open elsewhere").toggle(choice);
	}

	public void toggleRestoreEditorStateOnStartupCHB(boolean choice) {
		new CheckBox("Restore editor state on startup").toggle(choice);
	}

	public void toggleAllowInplaceSystemEditorsCHB(boolean choice) {
		new CheckBox("Allow in-place system editors").toggle(choice);
	}

	public void toggleOpenWhenUsingArrowKeysGroup(boolean choice) {
		new CheckBox(new DefaultGroup("Open mode"),"Open when using arrow keys").toggle(choice);
	}

	public void toggleSelectOnHoverGroup(boolean choice) {
		new CheckBox(new DefaultGroup("Open mode"),"Select on hover").toggle(choice);
	}

	public void toggleSingleClickGroup(boolean choice) {
		new RadioButton(new DefaultGroup("Open mode"),"Single click").toggle(choice);
	}

	public void toggleDoubleClickGroup(boolean choice) {
		new RadioButton(new DefaultGroup("Open mode"),"Double click").toggle(choice);
	}

	public void toggleRenameResourceInlineIfAvailableCHB(boolean choice) {
		new CheckBox("Rename resource inline if available").toggle(choice);
	}

	public void toggleShowHeapStatusCHB(boolean choice) {
		new CheckBox("Show heap status").toggle(choice);
	}

}
