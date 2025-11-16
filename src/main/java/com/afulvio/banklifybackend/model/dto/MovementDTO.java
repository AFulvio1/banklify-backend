package com.afulvio.banklifybackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(
        name = "MovementDTO",
        description = "Data Transfer Object representing a single transaction or movement on a bank account."
)
public class MovementDTO {

    @Schema(
            description = "The IBAN of the account associated with this movement.",
            example = "IT60X0542811101000000123456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String accountIban;

    @Schema(
            description = "The timestamp when the transaction event occurred, formatted in ISO 8601 with milliseconds.",
            example = "2023-10-27T10:30:00.123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime eventTimestamp;

    @Schema(
            description = "The amount of the transaction. Positive for incoming, negative for outgoing movements.",
            example = "-250.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal amount;

    @Schema(
            description = "The type of the transaction (e.g., 'INCOMING_TRANSFER', 'OUTGOING_TRANSFER', 'DEPOSIT', 'WITHDRAWAL').",
            example = "OUTGOING_TRANSFER",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String transactionType;

    @Schema(
            description = "A detailed description or reason for the transaction.",
            example = "Monthly gym subscription",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;

}
