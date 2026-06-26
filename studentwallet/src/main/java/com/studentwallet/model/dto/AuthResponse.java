package com.studentwallet.model.dto;

public class AuthResponse {

    private String token;
    private String userId;
    private String displayName;
    private String email;

    public AuthResponse() {
    }

    public AuthResponse(String token, String userId, String displayName, String email) {
        this.token = token;
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
