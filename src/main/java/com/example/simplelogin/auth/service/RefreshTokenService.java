package com.example.simplelogin.auth.service;

import com.example.simplelogin.auth.entity.RefreshToken;
import com.example.simplelogin.auth.model.RefreshTokenResponse;

public interface RefreshTokenService {

    RefreshTokenResponse create(String userId);

    RefreshTokenResponse getRefreshToken(RefreshToken refreshToken);

    void delete(String id);

}
