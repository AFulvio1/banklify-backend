package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.exception.AccountNotFoundException;
import com.afulvio.banklifybackend.model.dto.BalanceDTO;
import com.afulvio.banklifybackend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing bank accounts and retrieving account-specific information")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{iban}/balance")
    @Operation(
            summary = "Retrieve account balance by IBAN",
            description = "Fetches the available and ledger balance for a specific bank account using its IBAN. Requires authentication"
    )
    public ResponseEntity<BalanceDTO> getAccountBalance(@PathVariable String iban) throws AccountNotFoundException {
        BalanceDTO balance = accountService.getBalance(iban);
        return ResponseEntity.ok(balance);
    }
}