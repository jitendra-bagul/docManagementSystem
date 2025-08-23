package com.gov.docmanagement.service;

import com.gov.docmanagement.exception.WrongPasswordException;
import com.gov.docmanagement.model.User;
import com.gov.docmanagement.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

   // createUser
    // updateUser
  //  deleteUser
  //findUser
  //findAllUsers

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final String PASSWORD_REGEX =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public Boolean existsByUserName(String username) {
    return userRepository.existsByUsername(username);
    }

    public Optional<User> findByRoleId(Long roleId){
        return userRepository.findByRoleId(roleId);
    }

    public User save(User user) throws Exception {
        validatePassword(user);
        return userRepository.save(user);
    }

    public User validatePassword(User user){
        if(isValidPassword(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }else {
            throw new WrongPasswordException("Password format is wrong");
        }
        return user;
    }

    public User update(User user) throws Exception {
        validatePassword(user);
        return userRepository.save(user);
    }
}
