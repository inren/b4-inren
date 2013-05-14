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
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.securityinren.entity.Role;
import org.bricket.b4.securityinren.repository.RoleRepository;
import org.bricket.b4.securityinren.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "roleService")
@Transactional(readOnly = true)
@Slf4j
public class RoleServiceImpl extends B4ServiceImpl implements RoleService {
    @Resource
    private RoleRepository roleRepository;

    @Transactional
    @Override
    protected final void onInit() throws B4ServiceException {
        if (roleRepository.count() == 0) {
            List<Role> roles = new ArrayList<Role>();
            for (Roles role : Roles.values()) {
                Role r = new Role();
                r.setName(role.name());
                roles.add(r);
            }
            Iterable<Role> result = roleRepository.save(roles);
            log.info("created auto generated roles: " + result);
        }
        log.info("role service initialized");
    }

    @Override
    public Role saveRole(Role role) throws B4ServiceException {
        try {
            role = roleRepository.save(role);
        } catch (Exception e) {
            throw new B4ServiceException("error");
        }
        return role;
    }

    @Override
    public List<Role> loadAllRoles() throws B4ServiceException {
        try {
            Iterable<Role> roles = roleRepository.findAll();
            List<Role> res = new ArrayList<>();
            for (Role role : roles) {
                res.add(role);
            }
            return res;
        } catch (Exception e) {
            throw new B4ServiceException("error");
        }
    }
}
