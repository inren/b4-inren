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
package org.bricket.b4.mail.service;

import org.bricket.b4.core.service.B4ServiceException;

/**
 * @author Ingo Renner
 * @author Henning Teek
 * 
 */
public class MailServiceException extends B4ServiceException {
    private static final String MAIL_SERVICE = "mail.";

    public static final String SERVER_NOT_FOUND = "server.not.found";
    public static final String UNKNOWN_ERROR = "error.unknown";

    /**
     * @param key
     */
    public MailServiceException(String key) {
        super(key);
    }

    /**
     * @param key
     * @param cause
     */
    public MailServiceException(String key, Throwable cause) {
        super(key, cause);
    }

    @Override
    public String getKey() {
        return MAIL_SERVICE + super.getKey();
    }
}
