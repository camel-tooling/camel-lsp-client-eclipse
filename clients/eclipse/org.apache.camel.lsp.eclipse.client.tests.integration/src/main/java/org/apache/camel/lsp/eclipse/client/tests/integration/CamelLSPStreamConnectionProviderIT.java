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
package org.apache.camel.lsp.eclipse.client.tests.integration;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.apache.camel.lsp.eclipse.client.CamelLSPStreamConnectionProvider;
import org.junit.Test;

public class CamelLSPStreamConnectionProviderIT {

	@Test
	public void testStart() throws Exception {
		CamelLSPStreamConnectionProvider provider = new CamelLSPStreamConnectionProvider();
		provider.start();
		InputStream inputStream = provider.getInputStream();
		assertThat(inputStream).isNotNull();
		byte[] buffer = new byte[1024];
		boolean isConnected = false;
		int time = 0;
		while (!isConnected && time  < 10000 && inputStream.read(buffer) != -1) {
			isConnected = new String(buffer).contains("Connected to Language Server...");
			time += 1000;
		}
		provider.stop();
		assertThat(isConnected).isTrue();
	}
	
}
