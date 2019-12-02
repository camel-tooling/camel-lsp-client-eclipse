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
package com.github.cameltooling.eclipse.internal.l10n;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	private static final String BASE_NAME = "com.github.cameltooling.eclipse.internal.l10n.messages";
	public static String camelPreferencePageDescription;
	public static String camelCatalogVersionSettings;
	public static String camelAdditionalComponentSettings;
	static {
		NLS.initializeMessages(BASE_NAME, Messages.class);
	}
	
}
