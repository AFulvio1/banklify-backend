package com.afulvio.banklifybackend.controller;

import com.afulvio.banklifybackend.exception.InsufficientFundsException;
import com.afulvio.banklifybackend.model.dto.MovementDTO;
import com.afulvio.banklifybackend.model.dto.TransferDTO;
import com.afulvio.banklifybackend.service.TransactionService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for executing bank transfers and retrieving account transaction history.")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    @Operation(
            summary = "Execute a bank transfer",
            description = "Processes an outgoing bank transfer from the sender's IBAN to the receiver's IBAN. Requires valid authentication and sufficient funds."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transfer executed successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"message\": \"Transfer successfully completed\"}"))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request - Invalid IBAN, amount, or other transfer details",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"error\": \"Account not found or invalid transfer details\"}"))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Authentication required or token is invalid",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient funds or user not authorized to transfer from sender IBAN",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"error\": \"Insufficient funds\"}"))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error - Unexpected error during transfer processing",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"error\": \"Internal error during transfer processing\"}"))
    )
    public ResponseEntity<?> executeInternalTransfer(@Valid @RequestBody TransferDTO transferDTO) throws InsufficientFundsException, AccountNotFoundException {
        return getResponseEntity(transferDTO);
    }

    @PostMapping("/external/transfer")
    @Operation(
            summary = "Execute a bank transfer",
            description = "Processes an outgoing bank transfer from the sender's IBAN to the receiver's IBAN. Requires valid authentication and sufficient funds."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transfer executed successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"message\": \"Transfer successfully completed\"}"))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request - Invalid IBAN, amount, or other transfer details",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"error\": \"Account not found or invalid transfer details\"}"))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient funds or user not authorized to transfer from sender IBAN",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"error\": \"Insufficient funds\"}"))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error - Unexpected error during transfer processing",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"error\": \"Internal error during transfer processing\"}"))
    )
    public ResponseEntity<?> executeExternalTransfer(@Valid @RequestBody TransferDTO transferDTO) throws InsufficientFundsException, AccountNotFoundException {
        if (StringUtils.isBlank(transferDTO.getSenderName())) {
            throw new IllegalArgumentException("error.transfer.external.sender.name.not.found");
        }
        return getResponseEntity(transferDTO);
    }

    private ResponseEntity<?> getResponseEntity(@RequestBody @Valid TransferDTO transferDTO) throws InsufficientFundsException, AccountNotFoundException {
        transactionService.executeTransfer(transferDTO);
        return ResponseEntity.ok(Map.of("message", "transfer.success"));
    }

    @GetMapping("/{iban}/movements")
    @Operation(
            summary = "Retrieve latest account movements",
            description = "Fetches a list of the most recent transactions for a given account IBAN, ordered by timestamp in descending order. Supports limiting the number of results."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved account movements",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransferDTO.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Authentication required or token is invalid",
            content = @Content(schema = @Schema(hidden = true))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden - User does not have permission to view movements for this account",
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
    public ResponseEntity<List<MovementDTO>> getAccountMovements(
            @PathVariable String iban,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<MovementDTO> movements = transactionService.getLatestMovements(iban, page, size);
        return ResponseEntity.ok(movements);
    }
}
