package org.bricket.b4.securityinren.controller;

import org.bricket.b4.securityinren.entity.Group;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class GroupResourceAssembler extends ResourceAssemblerSupport<Group, GroupResource> {

    @Autowired
    ModelMapper modelmapper;

    public GroupResourceAssembler() {
        super(GroupController.class, GroupResource.class);
    }

    @Override
    public GroupResource toResource(Group group) {
        GroupResource resource = instantiateResource(group);
        modelmapper.map(group, resource);
        return resource;
    }

}
