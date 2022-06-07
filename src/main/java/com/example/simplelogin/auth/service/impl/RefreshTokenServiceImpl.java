package com.example.simplelogin.auth.service.impl;

import com.example.simplelogin.auth.entity.RefreshToken;
import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.model.RefreshTokenResponse;
import com.example.simplelogin.auth.repository.RefreshTokenRepository;
import com.example.simplelogin.auth.service.RefreshTokenService;
import com.example.simplelogin.auth.service.UserService;
import com.example.simplelogin.exception.ExpiredTokenException;
import com.example.simplelogin.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${simple.app.jwtRefreshExpirationInMs}")
    private Long refreshTokenDuration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public RefreshTokenResponse create(String userId) {
        User user = userService.findById(userId);
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
        RefreshToken refreshToken = new RefreshToken(user, UUID.randomUUID().toString(), refreshTokenDuration);
        return refreshTokenRepository.save(refreshToken).toRefreshTokenResponse();
    }

    @Override
    public RefreshTokenResponse getRefreshToken(RefreshToken refreshTokenRequest) {
        if (refreshTokenRequest.getExpiredAt() - System.currentTimeMillis() < 0)
            throw new ExpiredTokenException("Please make new sign in request again");
        return findByTokenOrThrowNotFound(refreshTokenRequest.getToken()).toRefreshTokenResponse();
    }

    @Transactional
    @Override
    public void delete(String id) {
        refreshTokenRepository.deleteByUser(userService.findById(id));
    }

    RefreshToken findByTokenOrThrowNotFound(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Refresh token not found"));
    }
}
