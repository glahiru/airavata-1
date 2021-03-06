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

package org.apache.airavata.client.impl;

import org.apache.airavata.client.api.CredentialStoreSecuritySettings;

/**
 * Implementation of credential store security settings class.
 */
public class CredentialStoreSecuritySettingsImpl implements CredentialStoreSecuritySettings {

    private String tokenId;
    private String portalUserId;
    private String gatewayId;

    public CredentialStoreSecuritySettingsImpl(String tokenId) {
        this.tokenId = tokenId;
    }

    public CredentialStoreSecuritySettingsImpl() {
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public void setPortalUser(String portalUserName) {
        this.portalUserId = portalUserName;
    }

    @Override
    public String getPortalUser() {
        return this.portalUserId;
    }

    @Override
    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    @Override
    public String getGatewayId() {
        return this.gatewayId;
    }

    public String getTokenId() {
        return tokenId;
    }

}
