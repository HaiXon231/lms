package com.cnpm.lms.domain;

public class LoginResponse {
    public Long userId;
    public String name;
    public String role;
    public String avatarUrl;

    public LoginResponse(Long userId, String name, String role, String avatarUrl) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }
}
