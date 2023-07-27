package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_createdAt")
    private Timestamp userCreatedAt;
//    private ZonedDateTime createdAt;

    @Column(name = "user_updatedAt")
    private Timestamp userUpdatedAt;

    @Column(name = "user_active")
    private Boolean userActiveStatus;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
