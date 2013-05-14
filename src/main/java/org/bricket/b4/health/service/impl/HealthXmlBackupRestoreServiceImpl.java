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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

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

import de.inren.frontend.common.backuprestore.EndMarker;

/**
 * Service to backup and restore all health data into xml.
 * 
 * @author Ingo Renner
 *
 */
@Service(value = "healthXmlBackupRestoreService")
@Transactional(readOnly = true)
@Slf4j
public class HealthXmlBackupRestoreServiceImpl extends B4ServiceImpl implements HealthXmlBackupRestoreService {
    private static final String INITIAL_HEALTH_SERVICE_DATA_XML = "initialHealthServiceData.xml";

    @Autowired
    private UserService userService;

    @Autowired
    private HealthSettingsService healthSettingsService;
    
    @Autowired
    private MeasurementRepository measurementRepository;
    
    @Resource
    HealthSettingsRepository healthSettingsRepository;

    @Override
    protected void onInit() throws B4ServiceException {
        userService.init();
        healthSettingsService.init();
        initDataBase();
        log.info("Health backup and restore service initialized");
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
    public void restoreFromXmlFile(File file) throws B4ServiceException {
        // TODO Rechteprüfung
        XMLDecoder d;
        try {
            d = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            throw new B4ServiceException("restoreFromXmlFileFailed", e);
        }
        boolean finished = false;
        Long currentId = null;
        UidMapping currentMapping = null;
        HealthSettings currentSettings = null;
        Measurement currentMeasurement = null;
        do {
            try {
                Object o = d.readObject();
                log.info("readObject: " + o);
                if (o instanceof UidMapping) {
                    currentMapping = (UidMapping) o;
                    User user = userService.loadUserByEmail(currentMapping.getEmail());
                    currentId = user==null ? null : user.getId();
                    if (currentId != null) {
                        HealthSettings s = healthSettingsService.loadByUser(currentId);
                        if (s != null) {
                            healthSettingsService.deleteHealthSettings(s);
                        }
                        List<Measurement> ms = measurementRepository.findByUid(currentId);
                        for (Measurement m : ms) {
                            measurementRepository.delete(m);
                        }
                    } else {
                        // TODO handle unknown user in mapping
                    }
                }
                if (o instanceof HealthSettings && currentId!=null) {
                    currentSettings = (HealthSettings) o;
                    currentSettings.setUid(currentId);
                    healthSettingsService.save(currentSettings);
                }
                if (o instanceof Measurement && currentId!=null) {
                    currentMeasurement = (Measurement) o;
                    currentMeasurement.setUid(currentId);
                    measurementRepository.save(currentMeasurement);
                }
                if (o instanceof EndMarker) {
                    finished = true;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                d.close();
                throw new B4ServiceException("restoreFromXmlFileFailed", e);
            }
        } while (!finished);
        d.close();
    }
    
    private void initDataBase() throws B4ServiceException {
        if (healthSettingsRepository.count() == 0) {
            File file = new File(getInitialConfigurationFolder(), INITIAL_HEALTH_SERVICE_DATA_XML);
            if (file.exists()) {
                log.info("HealthService is filled with initial data from: " + file.getAbsolutePath());
                restoreFromXmlFile(file);
            } else {
                log.info("If you need to init your db with custom data, put it here: " + file.getAbsolutePath());
            }
        }
    }
}
