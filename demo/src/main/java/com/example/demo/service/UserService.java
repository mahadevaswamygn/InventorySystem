package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.reposotory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setCreatedAt(Timestamp.from(Instant.now()));
//        waiting for brother
//       ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
//       user.setCreatedAt(now);

        newUser.setActiveStatus(false);

//        By initial state  set the role as by ourself
//        Role role = roleService.saveRole();
//        newUser.setRole(role);

        Role role=roleService.findById(2);
        newUser.setRole(role);
        try {
            userRepository.save(newUser);
        }catch (Exception exception){
            exception.printStackTrace();
            System.out.println(exception.getMessage());
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        List<User> allUsers=userRepository.findAll();
        return allUsers;
    }

    public User findUserById(Integer userId) {
        return userRepository.findById(userId).get();
    }

    public void saveTheUser(User user) {
        userRepository.save(user);
    }
}
