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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.securityinren.entity.Role;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.repository.RoleRepository;
import org.bricket.b4.securityinren.repository.UserRepository;
import org.bricket.b4.securityinren.service.RoleService;
import org.bricket.b4.securityinren.service.UserService;
import org.bricket.b4.securityinren.service.RoleService.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ingo Renner
 * @author Henning Teek
 * 
 */
@Service(value = "userService")
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl extends B4ServiceImpl implements UserService, UserDetailsService {
    @Resource
    UserRepository userRepository;

    @Resource
    RoleRepository roleRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    protected void onInit() throws B4ServiceException {
        roleService.init();

        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN.name());
            Role userRole = roleRepository.findByName(Roles.ROLE_USER.name());

            List<User> users = new ArrayList<User>();
            for (Users u : Users.values()) {
                User user = new User();
                user.setEmail(u.getEmail());
                user.setPassword(passwordEncoder.encode(u.getPassword()));
                user.setEnabled(true);

                switch (u) {
                case ADMIN:
                    user.setRoles(new HashSet<Role>(Arrays.<Role> asList(adminRole, userRole)));
                    break;
                case USER:
                    user.setRoles(new HashSet<Role>(Arrays.<Role> asList(userRole)));
                    break;
                }

                users.add(user);
            }
            Iterable<User> result = userRepository.save(users);
            log.info("created auto generated users: " + result);
        }
        log.info("user service initialized");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("no user for email: " + email);
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return userDetails;
    }

    /**
     * @param min
     * @param max
     * @return
     */
    private static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    /**
     * @param min
     * @param max
     * @return
     */
    private static String randomString(int min, int max) {
        int num = randomInt(min, max);
        byte b[] = new byte[num];
        for (int i = 0; i < num; i++) {
            b[i] = (byte) randomInt('a', 'z');
        }
        return new String(b);
    }

    @Override
    @Transactional
    public User saveUser(User user) throws B4ServiceException {
        try {
            if (user.isNew()) {
                // save user to generate id for salt source.
                final User u = userRepository.save(user);
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                u.setActivationKey(randomString(10, 12));
                log.debug("creating new user: {}", user);
                return userRepository.save(u);
            } else {
                final User u = userRepository.findOne(user.getId());
                if (user.getPassword() == null || "".equals(user.getPassword()) || user.getPassword().equals(u.getPassword())) {
                    // password not changed. keep old one.
                    user.setPassword(u.getPassword());
                    log.debug("updating user: {}", user);
                } else {
                    // password changed. encode it.
                    u.setPassword(passwordEncoder.encode(u.getPassword()));
                    log.debug("updating user (new password): {}", user);
                }
                return userRepository.save(user);
            }
        } catch (Exception e) {
            log.error("error saving user: " + user, e);
            throw new B4ServiceException("UserService", e);
        }
    }

    @Override
    @Transactional
    public List<User> saveUsers(List<User> users) throws B4ServiceException {
        // Save user objects separately. Special password handling!
        List<User> result = new ArrayList<User>();
        for (User user : users) {
            result.add(saveUser(user));
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws B4ServiceException {
        userRepository.delete(userRepository.findOne(user.getId()));
    }

    @Override
    public User loadUser(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public List<User> loadAllUser() {

        List<User> res = new ArrayList<>();
        Iterator<User> i = userRepository.findAll().iterator();
        while (i.hasNext()) {
            res.add((User) i.next());
        }
        return res;
    }

    @Override
    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User loadUserByEmailAndActivationKey(String email, String activationKey) {
        return userRepository.findByEmailAndActivationKey(email, activationKey);
    }

}
