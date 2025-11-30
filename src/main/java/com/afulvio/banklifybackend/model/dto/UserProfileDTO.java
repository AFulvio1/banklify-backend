package com.afulvio.banklifybackend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(
        name = "UserProfileDTO",
        description = "DTO representing a user's profile information."
)
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String taxCode;
    private String email;
    private String phoneNumber;
    private String address;
    private String houseNumber;
    private String zipCode;
    private String city;
    private String province;
}
