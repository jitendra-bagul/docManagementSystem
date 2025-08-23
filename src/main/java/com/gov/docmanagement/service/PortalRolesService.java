package com.gov.docmanagement.service;

import com.gov.docmanagement.model.PortalRoles;
import com.gov.docmanagement.repo.PortalRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortalRolesService {

    @Autowired
    private PortalRolesRepository portalRolesRepository;

    public PortalRoles save(PortalRoles portalRoles){
        return  portalRolesRepository.save(portalRoles);
    }

    public Optional<PortalRoles> findByPortalRole(PortalRoles portalRoles){
       return portalRolesRepository.findByPortalRoleByPortalRole(portalRoles.getPortalRole());
    }
}
