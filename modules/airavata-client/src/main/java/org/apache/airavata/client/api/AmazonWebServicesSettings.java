/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.client.api;

public interface AmazonWebServicesSettings {
	public String getAccessKeyId();
	public String getAMIId();
	public String getInstanceId();
	public String getInstanceType();
	public String getSecretAccessKey();
	public String getUsername();
	
	public void setAccessKeyId(String accessKeyId);
	public void setAMIId(String amiId);
	public void setInstanceId(String instanceId);
	public void setInstanceType(String instanceType);
	public void setSecretAccessKey(String secretAccessKey);
	public void setUsername(String username);
}
