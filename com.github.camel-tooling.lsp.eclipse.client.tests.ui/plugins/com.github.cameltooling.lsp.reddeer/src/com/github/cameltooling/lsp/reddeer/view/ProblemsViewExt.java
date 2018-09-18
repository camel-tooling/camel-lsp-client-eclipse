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
package com.github.cameltooling.lsp.reddeer.view;

import java.util.List;

import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;

public class ProblemsViewExt extends ProblemsView {
	
	public static final String ERRORS = "Errors";

	public void doubleClickProblem (String name, ProblemType type) {
		activate();
		List<TreeItem> items = new DefaultTree().getItems();
		for (TreeItem item : items) {
			if (type.equals(ProblemType.ERROR) && item.getText().startsWith(ERRORS)) {
				List<TreeItem> tmpList = item.getItems();
				for (TreeItem tmp : tmpList) {
					if (tmp.getText().contains(name)) {
						tmp.doubleClick();
						break;
					}
				}
				break;
			}
		}
	}
}
