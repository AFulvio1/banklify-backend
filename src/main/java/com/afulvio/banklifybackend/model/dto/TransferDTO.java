package com.afulvio.banklifybackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDTO {

    @NotBlank
    private String senderIban;

    @NotBlank
    private String receiverIban;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String description;
}
