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
package org.bricket.b4.securityinren.service.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.util.file.Folder;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.securityinren.entity.Group;
import org.bricket.b4.securityinren.entity.Role;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.repository.GroupRepository;
import org.bricket.b4.securityinren.repository.RoleRepository;
import org.bricket.b4.securityinren.repository.UserRepository;
import org.bricket.b4.securityinren.service.GroupService;
import org.bricket.b4.securityinren.service.RoleService;
import org.bricket.b4.securityinren.service.UserService;
import org.bricket.b4.securityinren.service.UsersXmlBackupRestoreService;
import org.bricket.b4.usersettings.entity.UserSettings;
import org.bricket.b4.usersettings.repository.UserSettingsRepository;
import org.bricket.b4.usersettings.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.inren.frontend.common.backuprestore.EndMarker;

/**
 * @author Ingo Renner
 *
 */
@Service(value = "usersXmlBackupRestoreService")
@Transactional(readOnly = true)
@Slf4j
public class UsersXmlBackupRestoreServiceImpl extends B4ServiceImpl implements UsersXmlBackupRestoreService {

    @Autowired
    private GroupService groupService;  

    @Resource
    private GroupRepository groupRepository;
    
    @Autowired
    private RoleService roleService;

    @Resource
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Resource
    UserRepository userRepository;

    @Autowired
    private UserSettingsService userSettingsService;
    
    @Resource
    UserSettingsRepository userSettingsRepository;
        
    @Override
    protected void onInit() throws B4ServiceException {
        roleService.init();
        groupService.init();
        userService.init();
        userSettingsService.init();
        
        initDataBase();
        
        File folder = new Folder(System.getProperty("java.io.tmpdir"), "inren-uploads");
        folder.mkdirs();
        File file = new File(folder, "userBackup.xml");
        try {
            FileUtils.writeStringToFile(file, dumpDbToXml());
            log.info("User data saved to " + file);
            restoreFromXmlFile(file);
            log.info("and restored again");
        } catch (IOException e) {
            throw new B4ServiceException(e.getMessage());
        }
        log.info("Users backup and restore service initialized");
    }

    @Override
    public String dumpDbToXml() throws B4ServiceException {
        XMLEncoder e;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        e = new XMLEncoder(new BufferedOutputStream(bos));

        // save roles
        Iterable<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            e.writeObject(role);
        }
        
        // save groups
        Iterable<Group> groups = groupRepository.findAll();
        for (Group group : groups) {
            e.writeObject(group);
        }
        
        // save users
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            // convert hibernate proxies to "real" sets
            Set<Group> g = new HashSet<Group>();
            g.addAll(user.getGroups());
            user.setGroups(g);
            
            Set<Role> r = new HashSet<Role>();
            r.addAll(user.getRoles());
            user.setRoles(r);
            
            e.writeObject(user);
            UserSettings settings = userSettingsService.loadByUser(user.getId());
            if (settings!=null) {
                e.writeObject(settings);
            }
        }
        // end marker and finish
        e.writeObject(new EndMarker());
        e.close();
        try {
            return bos.toString("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
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
        // cleanup database before restore
        userRepository.deleteAll();
        userSettingsRepository.deleteAll();
        groupRepository.deleteAll();
        roleRepository.deleteAll();
        Map<Long, Role> roleMap = new HashMap<Long, Role>();
        Map<Long, Group> groupMap = new HashMap<Long, Group>();
        Map<Long, Long> userMap = new HashMap<Long, Long>();
        do {
            try {
                Object o = d.readObject();
                log.info("readObject: " + o);
                // roles
                if (o instanceof Role) {
                    Role role = (Role) o;
                    Long id = role.getId();
                    role.setId(0L);
                    roleMap.put(id, roleRepository.save(role));
                }
                // groups
                if (o instanceof Group) {
                    Group group = (Group) o;
                    Long id = group.getId();
                    group.setId(0L);
                    groupMap.put(id, groupRepository.save(group));
                }
                // users and settings
                if (o instanceof User) {
                    User user = (User) o;
                    // Roles
                    Set<Role> roles = new HashSet<Role>();
                    for (Role role : user.getRoles()) {
                        roles.add(roleMap.get(role.getId()));
                    }
                    user.setRoles(roles);
                    // Groups
                    Set<Group> groups = new HashSet<Group>();
                    for (Group group : user.getGroups()) {
                        groups.add(groupMap.get(group.getId()));
                    }
                    user.setGroups(groups);
                    
                    Long id = user.getId();
                    user.setId(0L);
                    userMap.put(id, userRepository.save(user).getId());
                }
                if (o instanceof UserSettings) {
                    UserSettings userSettings = (UserSettings) o;
                    if (userSettings!=null) {
                        userSettings.setUid(userMap.get(userSettings.getId()));
                        userSettings.setId(0L);
                        userSettingsRepository.save(userSettings);
                    }
                }
                // end marker
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

    private void initDataBase() {
        return;
    }
    
}
