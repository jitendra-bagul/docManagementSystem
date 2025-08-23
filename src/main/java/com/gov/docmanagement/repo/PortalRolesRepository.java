package com.gov.docmanagement.repo;

import com.gov.docmanagement.model.PortalRoles;
import com.gov.docmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortalRolesRepository extends JpaRepository<PortalRoles, Long> {

    //PortalRoles findByPortalRoleByPortalRole(String portalRole); "SELECT p FROM PortalRoles p WHERE p.portal_role = :portalRole"

    @Query(value ="SELECT * FROM portal_role_m WHERE portal_role = :portalRole", nativeQuery = true)
    Optional<PortalRoles> findByPortalRoleByPortalRole(@Param("portalRole") String portalRole);

}
