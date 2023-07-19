package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_createdAt")
    private Timestamp createdAt;
//    private ZonedDateTime createdAt;

    @Column(name = "user_updatedAt")
    private Timestamp updatedAt;

    @Column(name = "user_active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
