package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.model.dto.UserProfileDTO;
import com.afulvio.banklifybackend.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@Tag(name = "Client Info", description = "APIs for retrieving client information")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/profile")
    @Operation(
            summary = "Retrieve current user's profile",
            description = "Fetches the profile information of the currently authenticated user"
    )
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        UserProfileDTO profile = clientService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

}
