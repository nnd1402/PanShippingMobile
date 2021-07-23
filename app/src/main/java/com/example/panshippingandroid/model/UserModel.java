package com.example.panshippingandroid.model;

import lombok.Data;

@Data
public class UserModel {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String address;
    private String country;
    private String phone;
}
