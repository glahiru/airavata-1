    /*
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements.  See the NOTICE file distributed with
     * this work for additional information regarding copyright ownership.
     * The ASF licenses this file to You under the Apache License, Version 2.0
     * (the "License"); you may not use this file except in compliance with
     * the License.  You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.airavata.model.appcatalog.computeresource;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

/**
 * Enumeration of security authentication and authorization mechanisms supported by Airavata. This enumeration just
 *  describes the supported mechanism. The corresponding security credentials are registered with Airavata Credential
 *  store.
 * 
 * USERNAME_PASSWORD:
 *  A User Name.
 * 
 * SSH_KEYS:
 *  SSH Keys
 * 
 */
@SuppressWarnings("all") public enum SecurityProtocol implements org.apache.thrift.TEnum {
  USERNAME_PASSWORD(0),
  SSH_KEYS(1),
  GSI(2),
  KERBEROS(3),
  OAUTH(4);

  private final int value;

  private SecurityProtocol(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static SecurityProtocol findByValue(int value) { 
    switch (value) {
      case 0:
        return USERNAME_PASSWORD;
      case 1:
        return SSH_KEYS;
      case 2:
        return GSI;
      case 3:
        return KERBEROS;
      case 4:
        return OAUTH;
      default:
        return null;
    }
  }
}
