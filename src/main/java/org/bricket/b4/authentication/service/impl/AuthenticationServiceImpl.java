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
package org.bricket.b4.authentication.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.bricket.b4.authentication.service.AuthenticationService;
import org.bricket.b4.authentication.service.AuthenticationServiceException;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.mail.service.MailPluginService;
import org.bricket.b4.security.entity.Role;
import org.bricket.b4.security.entity.User;
import org.bricket.b4.security.repository.RoleRepository;
import org.bricket.b4.security.repository.UserRepository;
import org.bricket.b4.security.service.RoleService;
import org.bricket.b4.security.service.impl.UserDetailsImpl;
import org.bricket.b4.security.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "authenticationService")
@Transactional(readOnly = true)
public class AuthenticationServiceImpl extends B4ServiceImpl implements AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
        
    @Resource
    UserRepository userRepository;

    @Resource
    RoleRepository roleRepository;

//    @Autowired
//    @Qualifier("authenticationManager")
//    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MailPluginService mailPluginService;

    @Transactional
    @Override
    protected void onInit() throws B4ServiceException {
        userService.init();
        roleService.init();
        mailPluginService.init();

        log.info("user service initialized");
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        final UserDetails user = userService.loadUserByUsername(email);
        if (user == null) {
            throw new UsernameNotFoundException("no user for email: " + email);
        }
        return user;
    }

    @Override
    @Transactional
    public void signupUser(User user) throws B4ServiceException {
        signupUser(user, false);
    }

    @Override
    @Transactional
    public void signupUser(User user, boolean sendActivationMail) throws B4ServiceException {
        try {
            
            User u = userRepository.save(user);
            log.debug("signup user: {}", u);

            if (sendActivationMail) {
                try {
                    mailPluginService.sendSignupMail(u);
                } catch (B4ServiceException e) {
                    throw new AuthenticationServiceException(
                            AuthenticationServiceException.AUTHENTICATION_SERVICE_SIGNUP_FAILED, e);
                }
            } else {
                // activateUser(u);
            }
        } catch (B4ServiceException e) {
            throw new AuthenticationServiceException(
                    AuthenticationServiceException.AUTHENTICATION_SERVICE_SIGNUP_FAILED, e);
        }
    }

    @Override
    public UserDetailsImpl authenticateUser(String email, String password) throws B4ServiceException {
        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    email, password));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetailsImpl) {
//                UserDetailsImpl user = ((UserDetailsImpl) authentication.getPrincipal());
//                log.debug("authenticated user: {}", user);
//                return user;
//            }
            throw new AuthenticationServiceException(
                    AuthenticationServiceException.AUTHENTICATION_SERVICE_AUTHENTICATION_FAILED);
        } catch (DisabledException de) {
            throw new AuthenticationServiceException(
                    AuthenticationServiceException.AUTHENTICATION_SERVICE_USER_DISABLED, de);
        } catch (LockedException le) {
            throw new AuthenticationServiceException(AuthenticationServiceException.AUTHENTICATION_SERVICE_USER_LOCKED,
                    le);
        } catch (BadCredentialsException bce) {
            throw new AuthenticationServiceException(
                    AuthenticationServiceException.AUTHENTICATION_SERVICE_BAD_CREDENTIALS, bce);
        } catch (AuthenticationException ae) {
            throw new AuthenticationServiceException(
                    AuthenticationServiceException.AUTHENTICATION_SERVICE_AUTHENTICATION_FAILED, ae);
        } catch (RuntimeException re) {
            throw new AuthenticationServiceException(
                    AuthenticationServiceException.AUTHENTICATION_SERVICE_AUTHENTICATION_FAILED, re);
        }
    }

//    @Override
//    @Transactional
//    public void activateUser(String email, String activationKey) throws B4ServiceException {
//        activateUser(userService.loadUserByEmailAndActivationKey(email, activationKey));
//    }
//
//    private void activateUser(User user) throws B4ServiceException {
//        if (user != null && !user.isEnabled()) {
//            try {
//                user.setEnabled(true);
//                userService.saveUser(user);
//                log.debug("activated user: {}", user);
//                return;
//            } catch (B4ServiceException bse) {
//                throw new AuthenticationServiceException(
//                        AuthenticationServiceException.AUTHENTICATION_SERVICE_ACTIVATION_FAILED, bse);
//            }
//        }
//        throw new AuthenticationServiceException(
//                AuthenticationServiceException.AUTHENTICATION_SERVICE_ACTIVATION_FAILED);
//    }

    @Override
    public List<GrantedAuthority> getAllAuthorities() {
        ArrayList<GrantedAuthority> res = new ArrayList<GrantedAuthority>();
        Iterable<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            res.add(new GrantedAuthorityImpl(role.getName()));
        }
        return res;
    }

    @Override
    public GrantedAuthority getGrantedAuthorityByID(String name) {
        return new GrantedAuthorityImpl(roleRepository.findByName(name).getName());
    }
}
