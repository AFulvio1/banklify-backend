package com.afulvio.banklifybackend.model.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class BalanceDTO {
    private String iban;
    private BigDecimal ledgerBalance;
    private BigDecimal availableBalance;
}
