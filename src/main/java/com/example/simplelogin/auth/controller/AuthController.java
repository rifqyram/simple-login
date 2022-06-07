package com.example.simplelogin.auth.controller;

import com.example.simplelogin.auth.model.RefreshTokenRequest;
import com.example.simplelogin.auth.model.SignInResponse;
import com.example.simplelogin.auth.model.UserRequest;
import com.example.simplelogin.auth.model.UserResponse;
import com.example.simplelogin.auth.service.AuthService;
import com.example.simplelogin.auth.service.RefreshTokenService;
import com.example.simplelogin.utils.ValidationUtil;
import com.example.simplelogin.utils.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final ValidationUtil validationUtil;

    @Autowired
    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, ValidationUtil validationUtil) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.validationUtil = validationUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody  UserRequest request) {
        validationUtil.validate(request);
        UserResponse userResponse = authService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new WebResponse<>(
                                HttpStatus.CREATED.value(),
                                HttpStatus.CREATED,
                                "User created successfully",
                                userResponse
                        )
                );
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody  UserRequest request) {
        validationUtil.validate(request);
        SignInResponse signInResponse = authService.signIn(request);
        return ResponseEntity.ok(
                new WebResponse<>(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        "User signed in successfully",
                        signInResponse
                )
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        validationUtil.validate(refreshTokenRequest);
        SignInResponse signInResponse = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getToken());
        return ResponseEntity.ok(
                new WebResponse<>(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        "Token refreshed successfully",
                        signInResponse
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        String message = authService.logout(authentication);
        return ResponseEntity.ok(
                new WebResponse<>(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        message,
                        null
                )
        );
    }

}
