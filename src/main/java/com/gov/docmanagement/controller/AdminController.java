package com.gov.docmanagement.controller;

import com.gov.docmanagement.model.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
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

    public Role addRole(Role role){
        return role;
    }
}
