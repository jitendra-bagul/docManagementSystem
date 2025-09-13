package com.gov.docmanagement.controller;

import com.gov.docmanagement.ApplicationConstants;
import com.gov.docmanagement.model.PortalRoles;
import com.gov.docmanagement.model.Role;
import com.gov.docmanagement.model.User;
import com.gov.docmanagement.service.PortalRolesService;
import com.gov.docmanagement.service.RoleService;
import com.gov.docmanagement.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/citizen")
public class CitizenController {

    private static final Logger logger = LoggerFactory.getLogger(CitizenController.class);

    @Autowired
    RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private PortalRolesService portalRolesService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping("/createCitizen")
    public ResponseEntity<?> createCitizen(@Valid @RequestBody User user, BindingResult result) throws Exception {
        logger.info("createCitizen called ");

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        Role citizenRole = new Role();
        citizenRole.setRoleName(ApplicationConstants.CITIZEN);
        citizenRole.setDescription(ApplicationConstants.CITIZEN_USER);
        Optional <Role> roleExists = roleService.findByRoleName(ApplicationConstants.CITIZEN);

        PortalRoles citizenPortalRole = new PortalRoles();
        citizenPortalRole.setPortalRole(ApplicationConstants.CITIZEN);
        citizenPortalRole.setDescription(ApplicationConstants.CITIZEN_USER);
        Optional<PortalRoles> portalRoles = portalRolesService.findByPortalRole(citizenPortalRole);

        Boolean citizenExists = userService.existsByUserName(user.getUsername());

        if (citizenExists){
            logger.info("User with Username "+user.getUsername() + " already exists");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User with Username "+user.getUsername() + " already exists");
        }
        if (roleExists.isEmpty()) {
            logger.info("First Citizen role");
            roleService.saveRole(citizenRole);
            user.setRole(citizenRole);
        }else {
            user.setRole(roleExists.get());
        }
        userService.save(user);
        if(portalRoles.isEmpty()){
            logger.info("First Citizen role for Portal");
            citizenPortalRole.setRoleId(citizenRole);
            portalRolesService.save(citizenPortalRole);
        }
        logger.info("createCitizen end");
        //return new ResponseEntity<>(user, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/updateCitizen")
    public ResponseEntity<?> updateCitizen(@Valid @RequestBody User updatedUser, BindingResult result) throws Exception {
        logger.info("updateCitizen called for user"+updatedUser.getUsername());
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }
        if (null != updatedUser && null!= updatedUser.getUsername()) {
            Optional<User> existingUser = userService.findByUserName(updatedUser.getUsername());

            if (existingUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User "+updatedUser.getUsername()+ " not found Register yourself from 'Citizen Tab' ");
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
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Re-Check Entered User Details");
        }
        logger.info("updateCitizen end");
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/updatePassword/{username}")
    public ResponseEntity<?> updateCitizenPassword(
            @PathVariable String username,
            @RequestBody Map<String, String> passwordPayload) throws Exception {
        logger.info("updatePassword called for "+username);
        String oldPassword = passwordPayload.get("oldPassword");
        String newPassword = passwordPayload.get("newPassword");

        Optional<User> existingUserOpt = null;
        if(null != username){
            existingUserOpt = userService.findByUserName(username);
            assert Objects.requireNonNull(existingUserOpt).isPresent();
            User existingUser = existingUserOpt.get();
            // Verify old password
            if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
            }
            // Encrypt and update new password
            existingUser.setPassword(newPassword);
            userService.save(existingUser);
            return ResponseEntity.ok("Password updated successfully");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is incorrect");
        }

    }

    @GetMapping("/getAllCitizens")
    public ResponseEntity<?> getAllCitizens() {
        Role citizenRole = new Role();
        citizenRole.setRoleName(ApplicationConstants.CITIZEN);
        citizenRole.setDescription(ApplicationConstants.CITIZEN_USER);
        Optional <Role> roleExists = roleService.findByRoleName(ApplicationConstants.CITIZEN);
        if(roleExists.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findAllCitizens(roleExists.get().getId()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Citizens Not Found");
    }


}
