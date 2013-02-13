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
package org.bricket.b4.securityinren.controller;

import java.util.List;

import org.bricket.b4.core.controller.ResourceNotFoundException;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserResourceAssembler userResourceAssembler;

    @Autowired
    private GroupResourceAssembler groupResourceAssembler;

    @Autowired
    private RoleResourceAssembler roleResourceAssembler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<List<UserResource>> getUsers() {
        return new HttpEntity<List<UserResource>>(userResourceAssembler.toResources(userRepository.findAll()));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<UserResource> createUser(@RequestBody User user) {
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new HttpEntity<UserResource>(userResourceAssembler.toResource(userRepository.save(user)));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<UserResource> getUser(@PathVariable("userId") User user) {
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        return new HttpEntity<UserResource>(userResourceAssembler.toResource(user));
    }

    @RequestMapping(value = "/{userId}/roles", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<List<RoleResource>> getRoles(@PathVariable("userId") User user) {
        return new HttpEntity<List<RoleResource>>(roleResourceAssembler.toResources(user.getRoles()));
    }

    @RequestMapping(value = "/{userId}/groups", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<List<GroupResource>> getGroups(@PathVariable("userId") User user) {
        return new HttpEntity<List<GroupResource>>(groupResourceAssembler.toResources(user.getGroups()));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    @ResponseBody
    public HttpEntity<UserResource> updateUser(@PathVariable("userId") User user, @RequestBody User u) {
        if (user == null) {
            throw new ResourceNotFoundException();
        }

        user.setEmail(u.getEmail());
        return new HttpEntity<UserResource>(userResourceAssembler.toResource(userRepository.save(user)));
    }
}