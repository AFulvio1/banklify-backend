package com.afulvio.banklifybackend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Data
public class AccountEntity {

    @Id
    private String iban; // IBAN è la chiave primaria

    // Relazione molti a uno: molti account a un client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    // Utilizzo di BigDecimal per l'accuratezza finanziaria
    @Column(precision = 15, scale = 2)
    private BigDecimal ledgerBalance; // Saldo Contabile

    @Column(precision = 15, scale = 2)
    private BigDecimal availableBalance; // Saldo Disponibile

    private String status; // ACTIVE, BLOCKED

    private LocalDateTime openingDate;

}
