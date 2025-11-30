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

    @Schema(description = "Unique identifier for the transaction.", example = "987654321")
    private Long TransactionId;

    @Schema(
            description = "The timestamp when the transaction event occurred, formatted in ISO 8601 with milliseconds.",
            example = "2023-10-27T10:30:00.123"
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime eventTimestamp;

    @Schema(description = "The amount of the transaction. Positive for incoming, negative for outgoing movements.", example = "-250.00")
    private BigDecimal amount;

    @Schema(description = "A detailed description or reason for the transaction.", example = "Monthly gym subscription")
    private String description;

    @Schema(description = "The name of the counterparty involved in the transaction.", example = "Fitness Center Ltd.")
    private String counterpartyName;

}
