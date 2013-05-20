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
package de.inren.frontend.backup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.health.service.HealthXmlBackupRestoreService;
import org.bricket.b4.securityinren.service.UsersXmlBackupRestoreService;

import de.inren.frontend.common.panel.ABasePanel;

/**
 * 
 * Panel to make db backup into a zip container.
 * 
 * @author Ingo Renner
 * 
 */
@Slf4j
public class BackupPanel extends ABasePanel {

    final static int BUFFER = 2048;

    final String dirname;

    @SpringBean
    private UsersXmlBackupRestoreService usersXmlBackupRestoreService;
    
    @SpringBean
    private HealthXmlBackupRestoreService healthXmlBackupRestoreService;

    public BackupPanel(String id) {
        super(id);
        dirname = "SiteBackup" + "_" + Calendar.getInstance().getTime().toString().replace(" ", "_");
    }

    @Override
    protected void initGui() {
        add(getAllBackupLink("backup"));

    }

    private File createFilesInBackupDirectory() throws Exception {
        File dir = new File(dirname);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // TODO needs a registry for all services with backup routines
        String xml;
        xml = usersXmlBackupRestoreService.dumpDbToXml();
        FileUtils.writeStringToFile(new File(dir, "users.xml"), xml);
        
        xml = healthXmlBackupRestoreService.dumpDbToXml();
        FileUtils.writeStringToFile(new File(dir, "health.xml"), xml);
        return dir;
    }

    private File createZipArchive(String srcFolder) {
        final File zipFile = new File(srcFolder + ".zip");

        try {
            BufferedInputStream origin = null;

            FileOutputStream dest = new FileOutputStream(zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];

            File subDir = new File(srcFolder);
            String subdirList[] = subDir.list();
            for (String sd : subdirList) {
                // get a list of files from current directory
                File f = new File(srcFolder + "/" + sd);
                if (f.isDirectory()) {
                    String files[] = f.list();

                    for (int i = 0; i < files.length; i++) {
                        System.out.println("Adding: " + files[i]);
                        FileInputStream fi = new FileInputStream(srcFolder + "/" + sd + "/" + files[i]);
                        origin = new BufferedInputStream(fi, BUFFER);
                        ZipEntry entry = new ZipEntry(sd + "/" + files[i]);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                            out.flush();
                        }
                    }
                } else // it is just a file
                {
                    FileInputStream fi = new FileInputStream(f);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(sd);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                        out.flush();
                    }
                }
            }
            origin.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            log.info("createZipArchive threw exception: " + e.getMessage());
            return null;
        }
        return zipFile;
    }

    private Component getAllBackupLink(String id) {
        ResourceReference rr = new ResourceReference(dirname + ".zip") {
            @Override
            public IResource getResource() {
                return new ByteArrayResource("application/zip") {

                    @Override
                    protected byte[] getData(Attributes attributes) {
                        try {
                            return FileUtils.readFileToByteArray(createZipArchive(createFilesInBackupDirectory().getAbsolutePath()));
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                };
            }
        };
        return new ResourceLink(id, rr);
    }
}
