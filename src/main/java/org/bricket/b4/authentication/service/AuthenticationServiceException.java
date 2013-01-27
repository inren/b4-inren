/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bricket.b4.authentication.service;

import org.bricket.b4.core.service.B4ServiceException;

/**
 * @author Ingo Renner
 * @author Henning Teek
 * 
 */
public class AuthenticationServiceException extends B4ServiceException {
    private static final String AUTHENTICATION_SERVICE = "authentication.";

    public static final String AUTHENTICATION_SERVICE_ACTIVATION_FAILED = "activation.failed";
    public static final String AUTHENTICATION_SERVICE_AUTHENTICATION_FAILED = "authentication.failed";
    public static final String AUTHENTICATION_SERVICE_SIGNUP_FAILED = "signup.failed";
    public static final String AUTHENTICATION_SERVICE_USER_DISABLED = "user.disabled";
    public static final String AUTHENTICATION_SERVICE_USER_LOCKED = "user.locked";
    public static final String AUTHENTICATION_SERVICE_BAD_CREDENTIALS = "bad.credentials";
    public static final String AUTHENTICATION_SERVICE_ASSIGN_ROLE_FAILED = "assign.role.failed";

    /**
     * @param key
     */
    public AuthenticationServiceException(String key) {
        super(key);
    }

    /**
     * @param key
     * @param cause
     */
    public AuthenticationServiceException(String key, Throwable cause) {
        super(key, cause);
    }

    @Override
    public String getKey() {
        return AUTHENTICATION_SERVICE + super.getKey();
    }
}
