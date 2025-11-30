package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.model.dto.BalanceDTO;
import com.afulvio.banklifybackend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing bank accounts and retrieving account-specific information.")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{iban}/balance")
    @Operation(
            summary = "Retrieve account balance by IBAN",
            description = "Fetches the available and ledger balance for a specific bank account using its IBAN. Requires authentication."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved account balance",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceDTO.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Authentication required or token is invalid",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden - User does not have permission to access this account",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Account not found for the provided IBAN",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error - Something went wrong on the server side"
    )
    public ResponseEntity<BalanceDTO> getAccountBalance(@PathVariable String iban) throws AccountNotFoundException {
        BalanceDTO balance = accountService.getBalance(iban);
        return ResponseEntity.ok(balance);
    }
}