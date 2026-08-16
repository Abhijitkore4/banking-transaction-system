package com.abhijit.bankingsystem.controller;

import com.abhijit.bankingsystem.dto.LoginRequest;
import com.abhijit.bankingsystem.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody LoginRequest request) {

        String token = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return Map.of(
                "token", token
        );
    }
}