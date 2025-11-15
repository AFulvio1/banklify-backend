package com.afulvio.banklifybackend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "client")
@Data
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_sequence")
    @SequenceGenerator(name = "client_sequence", sequenceName = "client_id_seq", allocationSize = 1)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "tax_code", unique = true)
    private String taxCode;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @UpdateTimestamp
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<AccountEntity> accounts;
}