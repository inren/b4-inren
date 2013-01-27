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
package org.bricket.b4.core.wicket;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.authentication.service.AuthenticationService;
import org.bricket.b4.core.service.B4ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.apache.wicket.authentication.AuthenticatedWebSession;

/**
 * @author Henning Teek
 * @author Ingo Renner
 */
public class BricketWebSession extends AuthenticatedWebSession {
    private final Logger log = LoggerFactory.getLogger(BricketWebSession.class);

    @SpringBean
    private AuthenticationService authenticationService;


    private UserDetails user;

    public BricketWebSession(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            this.user = authenticationService.authenticateUser(username, password);
            return true;
        } catch (B4ServiceException e) {
            log.error("authentication failed", e);
        }
        return false;
    }

    @Override
    public Roles getRoles() {
        Roles roles = new Roles();
        if (isSignedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                log.error("Unexpected: Authentication not found. No roles granted. SecurityContext="
                        + SecurityContextHolder.getContext().toString());
            } else {
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    roles.add(authority.getAuthority());
                }
            }
        }
        return roles;
    }

    public UserDetails getUser() {
        return user;
    }

}
