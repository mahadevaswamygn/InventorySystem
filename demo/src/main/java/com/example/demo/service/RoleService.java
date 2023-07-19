package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.reposotory.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role saveRole() {
        Role role=new Role();
        role.setCreatedAt(Timestamp.from(Instant.now()));
        role.setRoleName("USER");
        role.setFlag("Active");
        role.setAdmin(false);
        roleRepository.save(role);
        return role;
    }

    public Role findById(int roleId) {
        return roleRepository.findById(roleId).get();
    }
}
