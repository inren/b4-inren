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
package org.bricket.b4.storehouse.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.core.service.UncheckedRuntimeException;
import org.bricket.b4.storehouse.service.StorehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

/**
 * @author Ingo Renner
 *
 */
@Service("storehouseService")
public class StorehouseServiceImpl extends B4ServiceImpl implements StorehouseService {
    private final Logger log = LoggerFactory.getLogger(StorehouseServiceImpl.class);

    @Override
    protected final void onInit() {
        log.info("storehouse service initialized");
    }

    @Override
    public final File getDataFile(String digest) {
        return getDataFile(null, digest);
    }

    @Override
    public final File getDataFile(String area, String digest) {
        if (digest == null) {
            throw new UncheckedRuntimeException("Fatal error: digest must not be null.");
        }
        if (area != null && !"".equals(area)) {
            File areaFile = new File(getDataDir(), area);
            if (!areaFile.exists() && !areaFile.mkdirs()) {
                throw new UncheckedRuntimeException("Fatal error: unable to create area : ["
                        + areaFile.getAbsolutePath() + "].");
            }
            return new File(areaFile, createFilename(digest));
        } else {
            return new File(getDataDir(), createFilename(digest));
        }
    }

    private String createFilename(String digest) {
        String extension = ".data";
        StringBuffer fname = new StringBuffer(digest.length() + extension.length());
        char[] chars = digest.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            fname.append(chars[i]);
            if (i % 2 != 0 && i < chars.length - 1) {
                fname.append('/');
            }
        }
        return fname.append(extension).toString();
    }

    public final File getBaseDataDir() {
        return getDataDir();
    }

    private File getDataDir() {
        final File storehouse = new File(System.getProperty("catalina.home") + "/data/storehouse");
        if (!storehouse.exists() && !storehouse.mkdirs()) {
            throw new UncheckedRuntimeException("Fatal error: unable to create dataDir : ["
                    + storehouse.getAbsolutePath() + "].");
        }
        return storehouse;
    }

    @Override
    public final String doImport(InputStream inputStream, byte[] md5sum) {
        return doImport(null, inputStream, md5sum);
    }

    @Override
    public final String doImport(String area, InputStream inputStream, byte[] md5sum) {
        final String digest = toHexString(md5sum);
        return doImport(area, inputStream, digest);
    }

    @Override
    public final String doImport(String area, InputStream inputStream, String digest) {
        OutputStream outputStream = null;
        try {
            // check for existing data with same digest
            File data = getDataFile(area, digest);
            if (data.exists()) {
                if (!data.delete()) {
                    throw new IOException("could not delete file; " + data.getPath());
                }
                log.warn("Data already exists for file {}. It will be replaced.", digest);
            }
            data = createFile(data);
            outputStream = new FileOutputStream(data);
            IOUtils.copy(inputStream, outputStream);

            log.debug("saved file: {}", digest);

            return digest;
        } catch (IOException e) {
            log.error("import error", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("error closing outputstream", e);
                }
            }
        }
        return null;
    }

    public final File createFile(File file) throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IOException("could not create directories for file; " + file.getPath());
        }
        if (!file.createNewFile()) {
            throw new IOException("could not create file: " + file.getPath());
        }
        return file;
    }

    private String toHexString(byte[] md5hashAsByteArray) {
        final Hex hex = new Hex();
        return new String(hex.encode(md5hashAsByteArray));
    }
}
