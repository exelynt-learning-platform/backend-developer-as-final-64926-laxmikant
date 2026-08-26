package com.booking.resourcebooking.controller;

import com.booking.resourcebooking.dto.LoginRequest;
import com.booking.resourcebooking.dto.LoginResponse;
import com.booking.resourcebooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}