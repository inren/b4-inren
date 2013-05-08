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
package org.bricket.b4.health.service.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.health.entity.HealthSettings;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.HealthSettingsRepository;
import org.bricket.b4.health.repository.MeasurementRepository;
import org.bricket.b4.health.service.HealthSettingsService;
import org.bricket.b4.health.service.HealthXmlBackupRestoreService;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ingo Renner
 *
 */
@Service(value = "healthXmlBackupRestoreService")
@Transactional(readOnly = true)
@Slf4j
public class HealthXmlBackupRestoreServiceImpl extends B4ServiceImpl implements HealthXmlBackupRestoreService {
    @Autowired
    UserService userService;

    @Autowired
    HealthSettingsService healthSettingsService;
    
    @Autowired
    private MeasurementRepository measurementRepository;
    
    @Resource
    HealthSettingsRepository healthSettingsRepository;

    @Override
    protected void onInit() throws B4ServiceException {
        userService.init();
        healthSettingsService.init();
        log.info("Health backup and restore service initialized");
        String xml = dumpDbToXml("admin@localhost");
        log.info("Dumping admin@localhost settings:\n" + xml);
        try {
            File file = new File("/tmp/inrenHealthDump.xml");
            if (file.exists()) {
                file.delete();
            }
            FileUtils.writeStringToFile(file, xml);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        restoreFromXml("");
    }
    
    @Override
    public String dumpDbToXml(String email) throws B4ServiceException {
        XMLEncoder e;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        e = new XMLEncoder(new BufferedOutputStream(bos));
        List<User> users = new ArrayList<User>();
        if (email==null) {
            users.addAll(userService.loadAllUser());
        } else {
            User u = userService.loadUserByEmail(email);
            if (u!=null) {
                users.add(u);
            }
        }
        for (User u : users) {
            UidMapping um = new UidMapping(u.getId(), u.getEmail());
            e.writeObject(um);
            HealthSettings s = healthSettingsRepository.findByUid(u.getId());
            if (s!=null) {
                e.writeObject(s);
            }
            List<Measurement> measurements = measurementRepository.findByUid(u.getId());
            for (Measurement m : measurements) {
                e.writeObject(m);
            }
        }
        e.writeObject(new EndMarker());
        e.close();
        try {
            return bos.toString("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }

    @Override
    public String dumpDbToXml() throws B4ServiceException {
        return dumpDbToXml(null);
    }

    @Override
    public void restoreFromXml(String xml) throws B4ServiceException {
        File file = new File("/tmp/inrenHealthDump.xml");
        if (!file.exists()) {
            log.info("nothing to import");
            return;
        }
        log.info("Testmode try to import from " + file.getAbsolutePath());
        try {
            xml = FileUtils.readFileToString(file);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            return;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        XMLDecoder d = new XMLDecoder(bais);
        boolean finished = false;
        do {
            try {
                Object o = d.readObject();
                log.info(o.toString());
                if (o instanceof EndMarker) {
                    finished = true;
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                finished = true;
            }
            
        } while (!finished);
        d.close();
    }

}
