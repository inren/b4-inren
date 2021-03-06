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
package de.inren.frontend.common.session;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.authentication.service.AuthenticationService;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.securityinren.service.impl.UserDetailsImpl;
import org.bricket.b4.usersettings.entity.UserSettings;
import org.bricket.b4.usersettings.service.UserSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//import org.apache.wicket.authentication.AuthenticatedWebSession;

/**
 * @author Henning Teek
 * @author Ingo Renner
 */
public class B4WebSession extends WebSession {
    private final Logger log = LoggerFactory.getLogger(B4WebSession.class);

    @SpringBean
    private AuthenticationService authenticationService;

    @SpringBean
    private UserSettingsService userSettingsService;
    
    /** True when the user is signed in */
    private volatile boolean signedIn = false;

    private UserDetails user;

    private UserSettings userSettings;
    
    public UserSettings getUserSettings() {
        return userSettings;
    }

    public B4WebSession(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    public boolean authenticate(String username, String password) {
        try {
            user = authenticationService.authenticateUser(username, password);
            
            userSettings = userSettingsService.loadByUser(((UserDetailsImpl) user).getUid());
            
            return true;
        } catch (B4ServiceException e) {
            log.error("authentication failed", e);
        }
        return false;
    }

    public Collection<String> getRoles() {
        Collection<String> roles = new ArrayList<String>();
        if (isSignedIn()) {
            for (GrantedAuthority authority : user.getAuthorities()) {
                roles.add(authority.getAuthority());
            }
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication == null) {
//                log.error("Unexpected: Authentication not found. No roles granted. SecurityContext=" + SecurityContextHolder.getContext().toString());
//            } else {
//            }
        }
        return roles;
    }

    /**
     * Try to logon the user. It'll call {@link #authenticate(String, String)} to do the real work and that is what you need to subclass to provide your own
     * authentication mechanism.
     * 
     * @param username
     * @param password
     * @return true, if logon was successful
     */
    public final boolean signIn(final String username, final String password) {
        signedIn = authenticate(username, password);
        if (signedIn) {
            bind();
        }
        return signedIn;
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    /**
     * Sign the user out.
     */
    public void signOut() {
        signedIn = false;
    }

    public UserDetails getUser() {
        return user;
    }

    /**
     * Call signOut() and remove the logon data from where ever they have been persisted (e.g. Cookies)
     */
    @Override
    public void invalidate() {
        signOut();
        userSettings = null;
        user = null;
        super.invalidate();
    }

}
