package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.model.dto.BalanceDTO;
import com.afulvio.banklifybackend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{iban}/balance")
    public ResponseEntity<BalanceDTO> getAccountBalance(@PathVariable String iban) {
        try {
            // NOTA: Qui, in un sistema reale, dovresti verificare che l'utente autenticato
            // sia il proprietario del conto tramite l'IBAN
            BalanceDTO balance = accountService.getBalance(iban);
            return ResponseEntity.ok(balance);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // NOTA: Si dovrebbero aggiungere endpoint per la creazione del conto, ecc.
}