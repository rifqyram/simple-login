package com.example.simplelogin.auth.model;

public class RefreshTokenResponse {

    private String id;

    private UserResponse user;

    private String token;

    private Long expiresAt;

    public RefreshTokenResponse(String id, UserResponse user, String token, Long expiresAt) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public RefreshTokenResponse() {
    }

    public String getId() {
        return id;
    }

    public UserResponse getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }
}
