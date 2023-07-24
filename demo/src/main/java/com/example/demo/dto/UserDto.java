package com.example.demo.dto;

import lombok.Data;

@Data
public class UserDto {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userConfirmPassword;
    private Integer userRoleId;
}
