package com.example.simplelogin.auth.service.impl;

import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.entity.UserDetailImpl;
import com.example.simplelogin.auth.model.RefreshTokenResponse;
import com.example.simplelogin.auth.model.SignInResponse;
import com.example.simplelogin.auth.model.UserRequest;
import com.example.simplelogin.auth.model.UserResponse;
import com.example.simplelogin.auth.repository.UserRepository;
import com.example.simplelogin.auth.service.AuthService;
import com.example.simplelogin.auth.service.RefreshTokenService;
import com.example.simplelogin.constant.ResponseMessage;
import com.example.simplelogin.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public UserResponse signUp(UserRequest request) {
        User save = userRepository.save(request.toCreateUser(passwordEncoder));
        return save.toUserResponse();
    }

    @Override
    public SignInResponse signIn(UserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(userDetail);
        RefreshTokenResponse refreshTokenResponse = refreshTokenService.create(userDetail.getId());

        log.info("User {} signed in at {}", userDetail.getUsername(), new Date());

        return new SignInResponse(
                userDetail.getUsername(),
                refreshTokenResponse.getToken(),
                jwtToken
                );
    }

    @Override
    public String logout(Authentication authentication) {
        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        refreshTokenService.delete(userDetail.getId());
        log.info("User {} signed out", userDetail.getUsername());
        return ResponseMessage.LOGOUT_SUCCESS;
    }

}
