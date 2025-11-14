package com.cnpm.lms.domain;

public class LoginResponse {
    public Long userId;
    public String name;
    public String role;

    public LoginResponse(Long userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
}
