package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.exception.InsufficientFundsException;
import com.afulvio.banklifybackend.model.dto.MovementDTO;
import com.afulvio.banklifybackend.model.dto.TransferDTO;
import com.afulvio.banklifybackend.service.TransactionService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for executing bank transfers and retrieving account transaction history")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    @Operation(
            summary = "Execute a bank transfer",
            description = "Processes an outgoing bank transfer from the sender's IBAN to the receiver's IBAN. Requires valid authentication and sufficient funds"
    )
    public ResponseEntity<String> executeInternalTransfer(@Valid @RequestBody TransferDTO transferDTO) throws InsufficientFundsException, AccountNotFoundException {
        return getResponseEntity(transferDTO);
    }

    @PostMapping("/external/transfer")
    @Operation(
            summary = "Execute a bank transfer",
            description = "Processes an outgoing bank transfer from the sender's IBAN to the receiver's IBAN. Requires valid authentication and sufficient funds"
    )
    public ResponseEntity<String> executeExternalTransfer(@Valid @RequestBody TransferDTO transferDTO) throws InsufficientFundsException, AccountNotFoundException {
        if (StringUtils.isBlank(transferDTO.getSenderName())) {
            throw new IllegalArgumentException("error.transfer.external.sender.name.not.found");
        }
        return getResponseEntity(transferDTO);
    }

    private ResponseEntity<String> getResponseEntity(@RequestBody @Valid TransferDTO transferDTO) throws InsufficientFundsException, AccountNotFoundException {
        transactionService.executeTransfer(transferDTO);
        return ResponseEntity.ok("Transfer successfully completed");
    }

    @GetMapping("/{iban}/movements")
    @Operation(
            summary = "Retrieve latest account movements",
            description = "Fetches a list of the most recent transactions for a given account IBAN, ordered by timestamp in descending order. Supports limiting the number of results"
    )
    public ResponseEntity<List<MovementDTO>> getAccountMovements(
            @PathVariable String iban,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<MovementDTO> movements = transactionService.getLatestMovements(iban, page, size);
        return ResponseEntity.ok(movements);
    }
}
