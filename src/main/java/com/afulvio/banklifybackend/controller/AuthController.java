package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.model.request.LoginRequest;
import com.afulvio.banklifybackend.model.request.RegisterRequest;
import com.afulvio.banklifybackend.model.response.LoginResponse;
import com.afulvio.banklifybackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user registration and authentication (login)")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided details. Upon successful registration, a new bank account is typically created for the user"
    )
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterRequest request) {
        authService.registerNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successfully done");
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user and get JWT token",
            description = "Authenticates a user with email and password, returning a JWT token for subsequent authenticated requests, along with user IBAN and first name"
    )
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.authenticateAndGenerateToken(request);
        return ResponseEntity.ok(response);
    }
}
