package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.reposotory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void saveUser(UserDto userDto) throws Exception{
        User user = new User();
        user.setUserEmail(userDto.getUserEmail());
        user.setUserName(userDto.getUserName());
        user.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        user.setUserCreatedAt(Timestamp.from(Instant.now()));
//        waiting for brother
//       ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
//       user.setCreatedAt(now);

        user.setUserActiveStatus(false);

//        By initial state  set the role as by ourself
//        Role role = roleService.saveRole();
//        user.setRole(role);

        Role role=roleService.findById(userDto.getUserRoleId());
        user.setRole(role);
        try {
            userRepository.save(user);
        }catch (Exception exception){
            throw new Exception();
        }
    }

    public User findUserByEmail(String email) throws Exception {
        try {
            return userRepository.findByUserEmail(email);
        }catch (Exception exception){
            throw new Exception();
        }
    }

    public List<User> findAllUsers() throws Exception {
        List<User> allUsers = new ArrayList<>();
        try {
            allUsers=userRepository.findAll();
        }catch (Exception exception){
            throw new Exception();
        }
        return allUsers;
    }

    public User findUserById(Integer userId) throws Exception {
        try {
            return userRepository.findById(userId).get();
        }catch (Exception exception){
            throw new Exception();
        }
    }

    public void saveTheUser(User user) {
        userRepository.save(user);
    }

    public boolean bothPasswordsMatch(String userPassword, String userConfirmPassword) {
        return userPassword.equals(userConfirmPassword);
    }

    public void updateTheUser(User user, UserDto userDto) throws Exception {
        try {
            Role role=roleService.findById(userDto.getUserRoleId());
            user.setRole(role);
            user.setUserName(userDto.getUserName());
            user.setUserUpdatedAt(Timestamp.from(Instant.now()));
            user.setUserEmail(userDto.getUserEmail());
            userRepository.save(user);
        }catch (Exception exception){
            throw new Exception();
        }

    }
}
