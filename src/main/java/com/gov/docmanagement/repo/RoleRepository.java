package com.gov.docmanagement.repo;

import com.gov.docmanagement.model.Role;
import com.gov.docmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByRoleName(String roleName);//check if role already present

    Optional<Role> findById(Long id);

    /*@Query("SELECT u FROM User u WHERE u.role_id = :id")
    Optional<User> findByRoleId(@Param("id") Long id);*/


    //ArrayList<Role> findAll();//return all data

    //void deleteById(Long id);
}
