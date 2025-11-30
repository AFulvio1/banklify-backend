package com.afulvio.banklifybackend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
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

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "tax_code", unique = true)
    private String taxCode;

    @Column(name = "address")
    private String address;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<AccountEntity> accounts;
}