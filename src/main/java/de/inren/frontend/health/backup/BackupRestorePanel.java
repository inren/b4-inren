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
package de.inren.frontend.health.backup;

import java.util.Calendar;

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.health.service.HealthXmlBackupRestoreService;

import de.inren.frontend.common.panel.ABasePanel;

/**
 * @author Ingo Renner
 *
 */
@Slf4j
public class BackupRestorePanel extends ABasePanel {

    @SpringBean
    private HealthXmlBackupRestoreService healthXmlBackupRestoreService;
    
    public BackupRestorePanel(String id) {
        super(id);
    }

    @Override
    protected void initGui() {
        // Backup User
        add(getMyBackupLink("userLink"));
        // Backup all Users
        add(getAllBackupLink("allLink"));
        // Restore 
        
    }

    private Component getMyBackupLink(String id) {
        Calendar cal = Calendar.getInstance();
        
        final String key = "HealthBackup_" + getUser().getUsername() + "_" + cal.getTime().toString() + ".xml";
        
        ResourceReference rr = new ResourceReference(key) {

            @Override
            public IResource getResource() {
                return new ByteArrayResource("text/xml") {

                    @Override
                    protected byte[] getData(Attributes attributes) {
                        try {
                            String xml = healthXmlBackupRestoreService.dumpDbToXml(getUser().getUsername());
                            return xml.getBytes("UTF-8");
                        } catch (Exception e) {
                            log.error(e.getMessage(),e);
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                };
            }};
            
        ResourceLink  l = new ResourceLink(id, rr);
        return l;
    }

    private Component getAllBackupLink(String id) {
        Calendar cal = Calendar.getInstance();
        
        final String key = "HealthBackup_AllUser" + "_" + cal.getTime().toString() + ".xml";
        
        ResourceReference rr = new ResourceReference(key) {

            @Override
            public IResource getResource() {
                return new ByteArrayResource("text/xml") {

                    @Override
                    protected byte[] getData(Attributes attributes) {
                        try {
                            String xml = healthXmlBackupRestoreService.dumpDbToXml();
                            return xml.getBytes("UTF-8");
                        } catch (Exception e) {
                            log.error(e.getMessage(),e);
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                };
            }};
            
        ResourceLink  l = new ResourceLink(id, rr);
        return l;
    }


}
