package com.afulvio.banklifybackend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "client")
@Data
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String taxCode; // Codice Fiscale

    @Column(unique = true)
    private String email;

    // NOTA: In un'applicazione reale, questa stringa è l'HASH della password (es. BCrypt)
    private String passwordHash;

    private LocalDateTime registrationDate;

    // Relazione uno a molti (un cliente può avere più conti)
    // Mappato dall'attributo 'client' nella classe Account
    // @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    // private List<Account> accounts;
}