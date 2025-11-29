package com.afulvio.banklifybackend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(
        name = "TransferDTO",
        description = "Data Transfer Object for executing a bank transfer. All fields are required and validated."
)
public class TransferDTO {

    @Schema(
            description = "International Bank Account Number (IBAN) of the sender's account. This must be a valid and accessible account for the authenticated user.",
            example = "IT60X0542811101000000123456"
    )
    @NotBlank(message = "{error.validation.required}")
    private String senderIban;

    @Schema(
            description = "Name of the sender of the transfer.",
            example = "Mario Rossi"
    )
    private String senderName;

    @Schema(
            description = "International Bank Account Number (IBAN) of the receiver's account. This must be a valid IBAN.",
            example = "IT60Y0542811101000000654321"
    )
    @NotBlank(message = "{error.validation.required}")
    private String receiverIban;

    @Schema(
            description = "Name of the receiver of the transfer.",
            example = "Giovanni Rossi"
    )
    @NotBlank(message = "{error.validation.required}")
    private String receiverName;

    @Schema(
            description = "The amount to transfer. Must be a positive decimal number.",
            example = "150.75"
    )
    @NotNull
    @Positive
    private BigDecimal amount;

    @Schema(
            description = "A description or reason for the transfer.",
            example = "Payment for monthly rent"
    )
    @NotBlank(message = "{error.validation.required}")
    private String description;
}
