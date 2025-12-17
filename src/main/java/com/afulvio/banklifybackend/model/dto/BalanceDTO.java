package com.afulvio.banklifybackend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(
        name = "BalanceDTO",
        description = "Data Transfer Object representing the financial balance of a bank account"
)
public class BalanceDTO {

    @Schema(
            description = "The International Bank Account Number (IBAN) of the account",
            example = "IT60X0542811101000000123456"
    )
    private String iban;

    @Schema(
            description = "The ledger balance of the account, reflecting all processed transactions. This might include pending transactions.",
            example = "12500.75"
    )
    private BigDecimal ledgerBalance;

    @Schema(
            description = "The available balance of the account, which is the amount currently available for withdrawal or spending. This typically excludes any pending transactions or holds.",
            example = "12400.00"
    )
    private BigDecimal availableBalance;

}
