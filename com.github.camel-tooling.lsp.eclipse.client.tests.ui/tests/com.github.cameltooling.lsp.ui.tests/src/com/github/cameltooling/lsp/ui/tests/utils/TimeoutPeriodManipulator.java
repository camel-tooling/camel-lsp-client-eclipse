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

import org.eclipse.reddeer.common.properties.RedDeerProperties;
import org.eclipse.reddeer.common.wait.TimePeriod;

/**
 * @author fpospisi
 */
public class TimeoutPeriodManipulator {

	private static final String TIMEOUT_PERIOD_FACTOR_PROPERTY_NAME = RedDeerProperties.TIME_PERIOD_FACTOR.getName();

	/**
	 * Changes Timeout Period to required value.
	 * 
	 * @param value
	 */
	public static void setFactor(int value) {
		System.setProperty(TIMEOUT_PERIOD_FACTOR_PROPERTY_NAME, String.valueOf(value));
		TimePeriod.updateFactor();
	}

	/**
	 * Changes Timeout Period back to default.
	 */
	public static void clearFactor() {
		System.clearProperty(TIMEOUT_PERIOD_FACTOR_PROPERTY_NAME);
		TimePeriod.updateFactor();
	}
}
