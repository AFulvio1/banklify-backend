package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.model.dto.UserProfileDTO;
import com.afulvio.banklifybackend.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        UserProfileDTO profile = clientService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

}
