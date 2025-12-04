package com.afulvio.banklifybackend.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private static final String ERROR_KEY = "error";

    private String getMessage(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("JSON Parse Error", ex);
        String message = "Request body is malformed or contains invalid data";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation Error", ex);
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> getMessage(error.getDefaultMessage(), error.getField()))
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, errors));
    }

    @ExceptionHandler(MissingSenderNameException.class)
    public ResponseEntity<Map<String, String>> handleMissingSenderName(MissingSenderNameException ex) {
        log.error("Missing Sender Name Exception", ex);
        String message = getMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, message));
    }

    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<Map<String, String>> handleSameAccountTransfer(SameAccountTransferException ex) {
        log.error("Same Account Transfer Exception", ex);
        String message = getMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_KEY, message));
    }


    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredential(InvalidCredentialException ex) {
        log.error("Invalid Credential Exception", ex);
        String message = getMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(ERROR_KEY, message));
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleClientNotFound(ClientNotFoundException ex) {
        log.error("Client Not Found Exception", ex);
        String message = getMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, message));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAccountNotFound(AccountNotFoundException ex) {
        log.error("Account Not Found Exception", ex);
        String message = getMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_KEY, message));
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException ex) {
        log.error("Email Already Registered Exception", ex);
        String message = getMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(ERROR_KEY, message));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientFunds(InsufficientFundsException ex) {
        log.error("Insufficient Funds Exception", ex);
        String message = getMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(ERROR_KEY, message));
    }

}