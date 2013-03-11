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

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.health.entity.HealthSettings;
import org.bricket.b4.health.repository.HealthSettingsRepository;
import org.bricket.b4.health.service.HealthSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ingo Renner
 * 
 */
@Service(value = "healthSettingsService")
@Transactional(readOnly = true)
@Slf4j
public class HealthSettingsServiceImpl extends B4ServiceImpl implements HealthSettingsService {

    @Resource
    HealthSettingsRepository healthSettingsRepository;

    @Override
    protected void onInit() throws B4ServiceException {
        log.info("Health settings service initialized");
    }

    @Override
    public HealthSettings load(long id) {
        return healthSettingsRepository.findOne(id);
    }

    @Override
    public HealthSettings loadByUser(Long uid) throws B4ServiceException {
        return healthSettingsRepository.findByUid(uid);
    }

    @Override
    public HealthSettings save(HealthSettings healthSettings) throws B4ServiceException {
        try {
            return healthSettingsRepository.save(healthSettings);
        } catch (Exception e) {
            log.error("error saving healthSettings: " + healthSettings, e);
            throw new B4ServiceException("HealthSettingsService", e);
        }
    }

    @Override
    public void deleteHealthSettings(HealthSettings healthSettings) {
        healthSettingsRepository.delete(healthSettings);
    }

}
