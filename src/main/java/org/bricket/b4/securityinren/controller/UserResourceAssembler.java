package org.bricket.b4.securityinren.controller;

import org.bricket.b4.securityinren.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

    @Autowired
    ModelMapper modelmapper;

    public UserResourceAssembler() {
        super(UserController.class, UserResource.class);
    }

    @Override
    public UserResource toResource(User user) {
        UserResource resource = instantiateResource(user);
        modelmapper.map(user, resource);
        return resource;
    }

}
