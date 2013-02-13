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
package org.bricket.b4.core.service;

public class B4ServiceException extends Exception {
    private static final String SERVICE_ERROR = "service.error.";

    private String key;

    /**
     * @param key
     */
    public B4ServiceException(String key) {
        super();
        this.key = key;
    }

    public B4ServiceException(String key, Throwable cause) {
        super(cause);
        this.key = key;
    }

    public String getKey() {
        return SERVICE_ERROR + key;
    }
}
