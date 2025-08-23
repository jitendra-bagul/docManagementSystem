package com.gov.docmanagement.controller;

import com.gov.docmanagement.ApplicationConstants;
import com.gov.docmanagement.model.PortalRoles;
import com.gov.docmanagement.model.Role;
import com.gov.docmanagement.model.User;
import com.gov.docmanagement.model.UserRoleDetails;
import com.gov.docmanagement.service.PortalRolesService;
import com.gov.docmanagement.service.RoleService;
import com.gov.docmanagement.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {

    private static final Logger logger = LoggerFactory.getLogger(SuperAdminController.class);

    @Autowired
    RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private PortalRolesService portalRolesService;

    @PostMapping("/createSuperAdmin")
    public ResponseEntity<?> createSuperAdmin(@Valid @RequestBody User user, BindingResult result) throws Exception {
        logger.info("createSuperAdmin called ");

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }
        Role hodRole = new Role();
        hodRole.setRoleName(ApplicationConstants.HOD);
        hodRole.setDescription(ApplicationConstants.Head_Of_Department);
        Boolean roleExists = roleService.existsByRoleName(ApplicationConstants.HOD);

        PortalRoles superAdminRole = new PortalRoles();
        superAdminRole.setPortalRole(ApplicationConstants.SUPERADMIN);
        superAdminRole.setDescription(ApplicationConstants.SUPERADMIN_DESC);
        Optional<PortalRoles> portalRoles = portalRolesService.findByPortalRole(superAdminRole);

        Boolean superAdminExists = userService.existsByUserName(ApplicationConstants.HOD);

        if (roleExists && superAdminExists && portalRoles.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("SuperAdmin already exists");
        } else {
            superAdminRole.setRoleId(hodRole);
            user.setRole(hodRole);
            roleService.saveRole(hodRole);
            userService.save(user);
            portalRolesService.save(superAdminRole);

        }
        logger.info("createSuperAdmin end ");
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody User user, BindingResult result) throws Exception {
        logger.info("createAdmin called ");

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        Role aoRole = new Role();
        aoRole.setRoleName(ApplicationConstants.AO);
        aoRole.setDescription(ApplicationConstants.Account_Officer);
        Boolean roleExists = roleService.existsByRoleName(ApplicationConstants.AO);

        PortalRoles adminRole = new PortalRoles();
        adminRole.setPortalRole(ApplicationConstants.ADMIN);
        adminRole.setDescription(ApplicationConstants.ADMIN_DESC);
        Optional<PortalRoles> portalRoles = portalRolesService.findByPortalRole(adminRole);

        Boolean adminExists = userService.existsByUserName(user.getUsername());

        if (roleExists && adminExists && portalRoles.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Admin already exists");
        } else {
            adminRole.setRoleId(aoRole);
            user.setRole(aoRole);
            roleService.saveRole(aoRole);
            userService.save(user);
            portalRolesService.save(adminRole);
        }
        logger.info("createAdmin end");
        //return new ResponseEntity<>(user, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/updateAdmin")
    public ResponseEntity<?> updateAdmin(@Valid @RequestBody User updatedUser, BindingResult result) throws Exception {
        logger.info("updateAdmin called ");
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }
        PortalRoles adminRole = new PortalRoles();
        adminRole.setPortalRole(ApplicationConstants.ADMIN);
        Optional<PortalRoles> portalRoles = portalRolesService.findByPortalRole(adminRole);

        if (portalRoles.isPresent() && null != portalRoles.get().getRoleId()) {
            Optional<User> existingUser = userService.findByRoleId(portalRoles.get().getRoleId().getId());

            if (existingUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found, You can directly create Admin from provided button");
            }
            // Update editable fields only
            existingUser.get().setFirstName(updatedUser.getFirstName());
            existingUser.get().setLastName(updatedUser.getLastName());
            existingUser.get().setEmailId(updatedUser.getEmailId());
            existingUser.get().setPhoneNo(updatedUser.getPhoneNo());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                //userService.validatePassword(updatedUser);
                logger.info("User pw " + updatedUser.getPassword());
                existingUser.get().setPassword(updatedUser.getPassword());
                logger.info("Existing pw " + existingUser.get().getPassword());
            }
            userService.save(existingUser.get());
        }
        logger.info("updateAdmin end");
        return ResponseEntity.ok(updatedUser);
    }


    @GetMapping("/getAllRoles")
    public ResponseEntity<?> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/getAdminInfo")
    public ResponseEntity<?> getAdminDetails() {
        UserRoleDetails userRoleDetails = new UserRoleDetails();
        PortalRoles adminRole = new PortalRoles();
        adminRole.setPortalRole(ApplicationConstants.ADMIN);
        Optional<PortalRoles> portalRoles = portalRolesService.findByPortalRole(adminRole);

        if (portalRoles.isPresent() && null != portalRoles.get().getRoleId()) {
            Optional<User> user = userService.findByRoleId(portalRoles.get().getRoleId().getId());
            //Optional<Role> role=  roleService.existsById(portalRoles.get().getRoleId().getId());

            if (user.isPresent()) {
                userRoleDetails.setUser(user.get());
                userRoleDetails.setPortalRoles(portalRoles.get());
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(userRoleDetails);
    }

    // won't allow, just edit will be allowed.. just created
    @DeleteMapping("/deleteAdmin")
    public ResponseEntity<?> deleteAdminRole(@PathVariable Long id) {
        boolean deleted = roleService.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if resource doesn't exist
        }
    }

}
