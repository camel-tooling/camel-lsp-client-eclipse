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

import org.eclipse.reddeer.common.exception.RedDeerException;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.condition.TableHasRows;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;

public class FilteredSelectionDialog extends DefaultShell {

	public static final String TITLE = "";

	public FilteredSelectionDialog() {
		super(TITLE);
	}

	public FilteredSelectionDialog setText(String text) {
		new DefaultText(this).setText(text);
		return this;
	}

	public List<TableItem> getItems() {
		return new DefaultTable(this).getItems();
	}

	public FilteredSelectionDialog waitForItems() {
		new WaitUntil(new TableHasRows(new DefaultTable(this)), TimePeriod.LONG, false);
		return this;
	}

	public FilteredSelectionDialog selectItem(String text) {
		DefaultTable table = new DefaultTable(this);
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				org.eclipse.swt.widgets.TableItem[] item = table.getSWTWidget().getSelection();
				if (item.length > 0) {
					if (!item[0].getText().startsWith(text)) {
						table.getItem(text).select();
					}
				}
			}
		});
		return this;
	}

	public void ok() {
		List<TableItem> selectedItems = new DefaultTable(this).getSelectedItems();
		if (selectedItems.isEmpty()) {
			throw new RedDeerException("No item is selected");
		}
		new OkButton(this).click();
		new WaitWhile(new ShellIsAvailable(TITLE));
	}

	public void cancel() {
		new CancelButton(this).click();
		new WaitWhile(new ShellIsAvailable(TITLE));
	}

}
