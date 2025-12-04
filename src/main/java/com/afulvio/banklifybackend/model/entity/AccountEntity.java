package com.afulvio.banklifybackend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Data
public class AccountEntity {

    @Id
    @Column(name = "iban")
    private String iban;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @ToString.Exclude
    private ClientEntity client;

    @Column(precision = 15, scale = 2)
    private BigDecimal ledgerBalance;

    @Column(precision = 15, scale = 2)
    private BigDecimal availableBalance;

    @Column(name = "status")
    private String status;

    @UpdateTimestamp
    @Column(name = "opening_date")
    private LocalDateTime openingDate;

}
