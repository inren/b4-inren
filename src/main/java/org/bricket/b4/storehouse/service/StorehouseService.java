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
package org.bricket.b4.storehouse.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bricket.b4.core.service.B4Service;

/**
 * @author Ingo Renner
 *
 */
public interface StorehouseService extends B4Service {
    String doImport(InputStream inputStream, byte[] md5sum);

    File getDataFile(String digest);

    /**
     * @param area
     *            of Storehouse Fiselsystem or null
     * @param digest
     * @return
     */
    File getDataFile(String area, String digest);

    /**
     * @param area
     *            of Storehouse Fiselsystem or null
     * @param inputStream
     * @param md5sum
     * @return
     */
    String doImport(String area, InputStream inputStream, byte[] md5sum);

    /**
     * @param area
     * @param inputStream
     * @param digest
     * @return
     */
    String doImport(String area, InputStream inputStream, String digest);

    /** Base dir */
    File getBaseDataDir();

    /** Create the real file including the path */
    File createFile(File file) throws IOException;
}
