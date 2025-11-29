package com.afulvio.banklifybackend.model.entity;

import com.afulvio.banklifybackend.model.TransactionType;
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

    @Column(name = "account_iban", nullable = false)
    private String accountIban;

    @Column(name = "sender_iban", nullable = false)
    private String senderIban;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "receiver_iban", nullable = false)
    private String receiverIban;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "description")
    private String description;

}
