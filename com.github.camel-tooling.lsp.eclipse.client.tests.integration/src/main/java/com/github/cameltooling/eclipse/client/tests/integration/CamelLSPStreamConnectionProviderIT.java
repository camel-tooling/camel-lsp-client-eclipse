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
package com.github.cameltooling.eclipse.client.tests.integration;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import com.github.cameltooling.eclipse.client.CamelLSPStreamConnectionProvider;
import org.junit.Test;

public class CamelLSPStreamConnectionProviderIT {

	@Test
	public void testStart() throws IOException {
		CamelLSPStreamConnectionProvider provider = new CamelLSPStreamConnectionProvider();
		provider.start();
		InputStream inputStream = provider.getInputStream();
		assertThat(inputStream).isNotNull();
		InputStream errorStream = provider.getErrorStream();
		assertThat(errorStream).isNotNull();
		byte[] buffer = new byte[1024];
		assertThat(errorStream.read(buffer)).isEqualTo(-1);
		provider.stop();
	}
	
}
