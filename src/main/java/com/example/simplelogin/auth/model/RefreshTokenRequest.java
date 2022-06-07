package com.example.simplelogin.auth.model;

import javax.validation.constraints.NotBlank;

public class RefreshTokenRequest {
    @NotBlank(message = "Token is required")
    private String token;

    public RefreshTokenRequest(String token) {
        this.token = token;
    }

    public RefreshTokenRequest() {
    }

    public String getToken() {
        return token;
    }
}
