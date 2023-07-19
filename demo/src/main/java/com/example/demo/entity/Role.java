package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "t_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_createdAt")
    private Timestamp createdAt;

    @Column(name = "role_updatedAt")
    private Timestamp updatedAt;

    @Column(name = "role_flag")
    private String flag;

    @Column(name = "role_isAdmin")
    private boolean isAdmin;

}
