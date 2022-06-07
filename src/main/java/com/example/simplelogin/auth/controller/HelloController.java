package com.example.simplelogin.auth.controller;

import com.example.simplelogin.auth.entity.UserDetailImpl;
import com.example.simplelogin.auth.model.UserResponse;
import com.example.simplelogin.auth.service.UserService;
import com.example.simplelogin.utils.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
    private final UserService userService;

    public HelloController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<?> hello(Authentication authentication) {
        UserResponse user = userService.getWithAuth(authentication);

        return ResponseEntity
                .ok()
                .body(new WebResponse<>(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        "Success",
                        user
                ));
    }

}
