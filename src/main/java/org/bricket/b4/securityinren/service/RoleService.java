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
package org.bricket.b4.securityinren.service;

import java.util.List;

import org.bricket.b4.core.service.B4Service;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.securityinren.entity.Role;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
public interface RoleService extends B4Service {
    /**
     * Enumeration of all default roles
     * 
     * @author Henning Teek
     */
    public enum Roles {
        ROLE_ADMIN, ROLE_USER;
    }

    Role saveRole(Role modelObject) throws B4ServiceException;
    
    List<Role> loadAllRoles() throws B4ServiceException;
    
}
