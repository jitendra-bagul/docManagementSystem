package com.gov.docmanagement.service;

import com.gov.docmanagement.model.Role;
import com.gov.docmanagement.model.User;
import com.gov.docmanagement.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {


    @Autowired
    RoleRepository roleRepository;

    public Role saveRole(Role role){
        roleRepository.save(role);
        return role;
    }

    public Boolean existsByRoleName(String roleName){
        return roleRepository.existsByRoleName(roleName);

    }

    public Optional<Role> existsById(Long id){
        return roleRepository.findById(id);
    }

    public Optional<Role> findByRoleName(String roleName){
        return roleRepository.findRoleByName(roleName);
    }

    public boolean deleteById(Long id){
        Optional<Role> role= roleRepository.findById(id);
        if(!role.isEmpty()){
            try {
                roleRepository.deleteById(id);
                return true;
            }catch (Exception e){
               System.out.println(" Issue while deleting Role with Id -"+id);
            }
        }
        return false;
    }
    //some methods are common in admin and superadmins so will write those here and will call from respective controllers
    //superadmin will create admin's role so he will call this and then admin can add all other roles



    public ResponseEntity<?> getAllRoles(){
        List<Role> roles=  roleRepository.findAll();
        return  ResponseEntity.status(HttpStatus.OK).body(roles);
    }


    /*public Role getRoleByName(String hod) {

    }*/

    /*public ResponseEntity<?> getAdminInfo() {
        User admin = userRepository.
    }*/
}
