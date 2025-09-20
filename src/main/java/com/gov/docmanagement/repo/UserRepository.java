package com.gov.docmanagement.repo;

import com.gov.docmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  ///@Query(value = "SELECT * FROM user_m WHERE username = :userName", nativeQuery = true)
  Boolean existsByUsername(String username);

  /*  @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findUserById(@Param("id") Long id);
*/
    @Query(value = "SELECT * FROM user_m WHERE role_id = :id", nativeQuery = true)
    Optional<User> findByRoleId(@Param("id") Long id);

  @Query(value = "SELECT * FROM user_m WHERE username = :username", nativeQuery = true)
  Optional<User> findByUserName(@Param("username") String username);

  @Query(value = "SELECT * FROM user_m WHERE role_id = :roleId", nativeQuery = true)
  List<User> findByCitizenRole(@Param("roleId") Long roleId);


}
