package com.gov.docmanagement.controller;

import com.gov.docmanagement.ApplicationConstants;
import com.gov.docmanagement.exception.DepartmentAlreadyExistException;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    /*
    getAllDepartments();
    addDepartment();
    getAllRoles();
    editRole();

    deleteRole();
    getAllUser();
    getUserById();

    checkAllApprovals();
    verifyDocument();
    addTag();
    addCategory();
    */

    @Autowired
    RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private PortalRolesService portalRolesService;
    @GetMapping("/hello")
    public String checkApp(){
        return "Hello";
    }

    //departments will be dropdowns of UI same values to be selected but to maintain flow, adding extra checks
    @PostMapping("/createClerk/{department}")
    public ResponseEntity<?> createClerk(@PathVariable String department, @Valid @RequestBody User user, BindingResult result) throws Exception {
        logger.info("createClerk called ");
        Role clerkRole = new Role();
        PortalRoles clerkPortalRole = new PortalRoles();
        Boolean roleExists;
        switch (department.toUpperCase()) {
            case ApplicationConstants.ADMINISTRATOR:
                clerkRole.setRoleName(ApplicationConstants.CLERK_ADMINISTRATOR);
                clerkRole.setDescription(ApplicationConstants.CLERK_ADMINISTRATOR_DESC);
                roleExists = roleService.existsByRoleName(ApplicationConstants.CLERK_ADMINISTRATOR);
                user.setUsername(ApplicationConstants.CLERK_ADMINISTRATOR);
                break;

            case ApplicationConstants.FINANCE:
                clerkRole.setRoleName(ApplicationConstants.CLERK_FINANCE);
                clerkRole.setDescription(ApplicationConstants.CLERK_FINANCE_DESC);
                roleExists = roleService.existsByRoleName(ApplicationConstants.CLERK_FINANCE);
                user.setUsername(ApplicationConstants.CLERK_FINANCE);
                break;

            case ApplicationConstants.WATER:
                clerkRole.setRoleName(ApplicationConstants.CLERK_WATER);
                clerkRole.setDescription(ApplicationConstants.CLERK_WATER_DESC);
                roleExists = roleService.existsByRoleName(ApplicationConstants.CLERK_WATER);
                user.setUsername(ApplicationConstants.CLERK_WATER);
                break;

            case ApplicationConstants.ELECTRIC:
                clerkRole.setRoleName(ApplicationConstants.CLERK_ELECTRIC);
                clerkRole.setDescription(ApplicationConstants.CLERK_ELECTRIC_DESC);
                roleExists = roleService.existsByRoleName(ApplicationConstants.CLERK_ELECTRIC);
                user.setUsername(ApplicationConstants.CLERK_ELECTRIC);
                break;

            case ApplicationConstants.SOLID_WASTE:
                clerkRole.setRoleName(ApplicationConstants.CLERK_SOLID_WASTE);
                clerkRole.setDescription(ApplicationConstants.CLERK_SOLID_WASTE_DESC);
                roleExists = roleService.existsByRoleName(ApplicationConstants.CLERK_SOLID_WASTE);
                user.setUsername(ApplicationConstants.CLERK_SOLID_WASTE);
                break;

            case ApplicationConstants.AUDIT:
                clerkRole.setRoleName(ApplicationConstants.CLERK_AUDIT);
                clerkRole.setDescription(ApplicationConstants.CLERK_AUDIT_DESC);
                roleExists = roleService.existsByRoleName(ApplicationConstants.CLERK_AUDIT);
                user.setUsername(ApplicationConstants.CLERK_AUDIT);
                break;

            case ApplicationConstants.CIVIL:
                clerkRole.setRoleName(ApplicationConstants.CLERK_CIVIL);
                clerkRole.setDescription(ApplicationConstants.CLERK_CIVIL_DESC);
                roleExists = roleService.existsByRoleName(ApplicationConstants.CLERK_CIVIL);
                user.setUsername(ApplicationConstants.CLERK_CIVIL);
                break;

            default:
                logger.info("Wrong Value passed in Department- "+department);
                throw new IllegalArgumentException("Unknown department: " + department);
        }

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }
        clerkPortalRole.setPortalRole(ApplicationConstants.OFFICE_USER);
        clerkPortalRole.setDescription(ApplicationConstants.OFFICE_USER_DESC);
        Optional<PortalRoles> portalRoles = portalRolesService.findByPortalRole(clerkPortalRole);
        Boolean clerkExists = userService.existsByUserName(user.getUsername());

        if (roleExists && clerkExists) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Clerk of Department "+department +" already exists");
        } else {
            user.setRole(clerkRole);
            roleService.saveRole(clerkRole);
            userService.save(user);
        }
        if (portalRoles.isEmpty()){// no relation between office user's role and clerk as it is common for all
            portalRolesService.save(clerkPortalRole);
        }
        logger.info("createClerk end");
        //return new ResponseEntity<>(user, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/getAllRoles")
    public ResponseEntity<?> getAllRoles(){
        return roleService.getAllRoles();
    }
}
