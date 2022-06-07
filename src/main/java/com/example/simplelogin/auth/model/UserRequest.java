package com.example.simplelogin.auth.model;

import com.example.simplelogin.auth.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private final String email;

    @NotBlank(message = "Password is required")
    @Min(value = 6, message = "Password must be at least 6 characters")
    private final String password;

    public UserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User toCreateUser(PasswordEncoder passwordEncoder) {
        return new User(email, passwordEncoder.encode(password));
    }
}
