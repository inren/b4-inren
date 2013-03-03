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
package org.bricket.b4.usersettings.service.impl;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.Application;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.usersettings.entity.UserSettings;
import org.bricket.b4.usersettings.repository.UserSettingsRepository;
import org.bricket.b4.usersettings.service.UserSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.agilecoders.wicket.Bootstrap;

/**
 * @author Ingo Renner
 *
 */
@Service(value = "userSettingsService")
@Transactional(readOnly = true)
@Slf4j
public class UserSettingsServiceImpl extends B4ServiceImpl implements UserSettingsService {

    @Resource
    UserSettingsRepository userSettingsRepository;

    @Override
    protected void onInit() throws B4ServiceException {
        // ??? nothing to do
        log.info("user settings service initialized");
    }

    @Override
    public UserSettings load(long id) {
        return userSettingsRepository.findOne(id);
    }

    @Override
    public UserSettings loadByUser(Long uid) throws B4ServiceException {
        UserSettings us = userSettingsRepository.findByUid(uid);
        if (us==null) {
           us = createDefaultUserSettings(uid);
        }
        return us;
    }

    private UserSettings createDefaultUserSettings(Long uid) throws B4ServiceException {
        UserSettings us = new UserSettings();
        us.setUid(uid);
        us.setTheme(Bootstrap.getSettings(Application.get()).getThemeProvider().defaultTheme().name());
        save(us);
        return us;
    }

    @Override
    public UserSettings save(UserSettings userSettings) throws B4ServiceException {
        try {
        return userSettingsRepository.save(userSettings);
        } catch (Exception e) {
            log.error("error saving userSettings: " + userSettings, e);
            throw new B4ServiceException("UserSettingsService", e);
        }
    }

    @Override
    public void deleteUserSettings(UserSettings userSettings) {
        userSettingsRepository.delete(userSettings);
    }
}
