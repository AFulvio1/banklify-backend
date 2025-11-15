package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.exception.InsufficientFundsException;
import com.afulvio.banklifybackend.model.dto.TransferDTO;
import com.afulvio.banklifybackend.model.entity.TransactionEntity;
import com.afulvio.banklifybackend.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<?> executeTransfer(@Valid @RequestBody TransferDTO transferDTO) {
        try {
            transactionService.executeTransfer(transferDTO);
            return ResponseEntity.ok(Map.of("message", "Bonifico eseguito con successo."));
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Errore interno durante l'elaborazione del bonifico."));
        }
    }

    @GetMapping("/{iban}/movements")
    public ResponseEntity<List<TransactionEntity>> getAccountMovements(
            @PathVariable String iban,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<TransactionEntity> movements = transactionService.getLatestMovements(iban, limit);
        return ResponseEntity.ok(movements);
    }
}
