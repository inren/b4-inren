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

import java.io.File;

import javax.annotation.PostConstruct;

public abstract class B4ServiceImpl implements B4Service {
    
    private static File initialConfigurationFolder = new File(System.getProperty("java.io.tmpdir"), "inren-dbinit");
    
    private boolean initialized = false;

    @Override
    @PostConstruct
    public final synchronized void init() throws B4ServiceException {
        if (initialized) {
            return;
        }
        onInit();
        initialized = true;
    }
    
    protected abstract void onInit() throws B4ServiceException;

    public static File getInitialConfigurationFolder() {
        return initialConfigurationFolder;
    }

}
