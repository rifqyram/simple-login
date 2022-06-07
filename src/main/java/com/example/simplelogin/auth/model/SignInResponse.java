package com.example.simplelogin.auth.model;

public class SignInResponse {

    private final String email;

    private final String refreshToken;

    private final String accessToken;

    public SignInResponse(String email, String refreshToken, String accessToken) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
