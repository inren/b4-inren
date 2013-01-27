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
package org.bricket.b4.authentication.service;

import java.util.List;

import org.bricket.b4.core.service.B4Service;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.security.entity.User;
import org.bricket.b4.security.service.impl.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
public interface AuthenticationService extends B4Service {

    void signupUser(User user, boolean sendActivationMail) throws B4ServiceException;

    UserDetailsImpl authenticateUser(String email, String password) throws B4ServiceException;

    //void activateUser(String email, String activationKey) throws B4ServiceException;

    UserDetails loadUserByEmail(String email);

    void signupUser(User user) throws B4ServiceException;

    GrantedAuthority getGrantedAuthorityByID(String name);

    List<GrantedAuthority> getAllAuthorities();
}
