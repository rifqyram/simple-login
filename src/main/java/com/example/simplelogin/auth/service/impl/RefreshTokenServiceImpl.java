package com.example.simplelogin.auth.service.impl;

import com.example.simplelogin.auth.entity.RefreshToken;
import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.model.RefreshTokenResponse;
import com.example.simplelogin.auth.model.SignInResponse;
import com.example.simplelogin.auth.repository.RefreshTokenRepository;
import com.example.simplelogin.auth.service.RefreshTokenService;
import com.example.simplelogin.auth.service.UserService;
import com.example.simplelogin.exception.ExpiredTokenException;
import com.example.simplelogin.exception.NotFoundException;
import com.example.simplelogin.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${simple.app.jwtRefreshExpirationInMs}")
    private Long refreshTokenDuration;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserService userService, JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public RefreshTokenResponse create(String userId) {
        User user = userService.findById(userId);
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
        RefreshToken refreshToken = new RefreshToken(user, UUID.randomUUID().toString(), System.currentTimeMillis() + refreshTokenDuration);
        return refreshTokenRepository.save(refreshToken).toRefreshTokenResponse();
    }

    @Override
    public SignInResponse verifyRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (!refreshToken.isPresent()) throw new NotFoundException("Refresh token not found");
        if (refreshToken.get().getExpiredAt() - System.currentTimeMillis() < 0) {
            refreshTokenRepository.delete(refreshToken.get());
            throw new ExpiredTokenException("Please make new sign in request again");
        }
        return refreshToken.map(RefreshToken::getUser).map(
                user -> {
                    String jwtToken = jwtUtils.generateTokenFromEmail(user.getEmail());
                    return new SignInResponse(user.getEmail(), refreshToken.get().getToken(), jwtToken);
                }
        ).orElseThrow(() -> new NotFoundException("Token not found"));
    }

    @Transactional
    @Override
    public void delete(String id) {
        refreshTokenRepository.deleteByUser(userService.findById(id));
    }
}
