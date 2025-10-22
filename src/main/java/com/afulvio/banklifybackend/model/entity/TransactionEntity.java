package com.afulvio.banklifybackend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    // IBAN del conto coinvolto in questa transazione
    @Column(name = "account_iban", nullable = false)
    private String accountIban;

    private LocalDateTime timestamp;

    // Importo con segno (positivo per accredito, negativo per addebito)
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    private String transactionType; // Esempio: OUTGOING_TRANSFER, INCOMING_TRANSFER

    private String description; // Causale

    // In un sistema reale, si potrebbe includere un riferimento all'altra transazione
    // per bonifici (es. 'relatedTransactionId')
}
