package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.model.request.LoginRequest;
import com.afulvio.banklifybackend.model.request.RegisterRequest;
import com.afulvio.banklifybackend.model.response.LoginResponse;
import com.afulvio.banklifybackend.service.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        try {
            authService.registerNewUser(request);
            return new ResponseEntity<>("Registration done", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticateAndGenerateToken(request);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Authentication error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
