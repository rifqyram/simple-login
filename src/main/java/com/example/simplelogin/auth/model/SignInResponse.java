package com.example.simplelogin.auth.model;

public class SignInResponse {

    private String email;

    private String refreshToken;

    private String accessToken;

    public SignInResponse(String email, String refreshToken, String accessToken) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public SignInResponse() {
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
