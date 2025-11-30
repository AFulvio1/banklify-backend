package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.model.request.LoginRequest;
import com.afulvio.banklifybackend.model.request.RegisterRequest;
import com.afulvio.banklifybackend.model.response.LoginResponse;
import com.afulvio.banklifybackend.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user registration and authentication (login).")
public class AuthController {

    private final ClientService clientService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided details. Upon successful registration, a new bank account is typically created for the user."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Registration successful",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"),
                    examples = @ExampleObject(value = "Registration done"))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input or user already exists",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"),
                    examples = @ExampleObject(value = "Email already registered"))
    )
    @ApiResponse(
            responseCode = "500",
            description = "An internal server error occurred"
    )
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid RegisterRequest request) {
        clientService.registerNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "registration.success"));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user and get JWT token",
            description = "Authenticates a user with email and password, returning a JWT token for subsequent authenticated requests, along with user IBAN and first name."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User authenticated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid credentials",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"),
                    examples = @ExampleObject(value = "Invalid email or password"))
    )
    @ApiResponse(
            responseCode = "500",
            description = "An internal server error occurred"
    )
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = clientService.authenticateAndGenerateToken(request);
        return ResponseEntity.ok(response);
    }
}
