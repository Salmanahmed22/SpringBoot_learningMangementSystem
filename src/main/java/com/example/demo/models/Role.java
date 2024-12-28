package com.example.demo.models;

public enum Role {
    ADMIN("ADMIN"),
    INSTRUCTOR("INSTRUCTOR"),
    STUDENT("STUDENT");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
