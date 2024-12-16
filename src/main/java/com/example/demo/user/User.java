package com.example.demo.user;

public interface User {
    Long getId();
    String getName();
    String getEmail();
    String updateProfile(String name , String email, String Password);
}
