package com.example.simplelogin.auth.service;

import com.example.simplelogin.auth.model.RefreshTokenResponse;
import com.example.simplelogin.auth.model.SignInResponse;

public interface RefreshTokenService {

    RefreshTokenResponse create(String userId);

    SignInResponse verifyRefreshToken(String token);

    void delete(String id);

}
