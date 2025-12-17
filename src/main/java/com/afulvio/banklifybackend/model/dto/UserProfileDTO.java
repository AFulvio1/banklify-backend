package com.afulvio.banklifybackend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(
        name = "UserProfileDTO",
        description = "DTO representing a user's profile information"
)
public class UserProfileDTO {

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's date of birth (ISO-8601 format)", example = "1990-05-20")
    private LocalDate birthDate;

    @Schema(description = "User's tax code or national identification number", example = "RSSMRA85M01H501U")
    private String taxCode;

    @Schema(description = "Primary email address for the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Primary phone number for the user, including country code", example = "+39 331 2345678")
    private String phoneNumber;

    @Schema(description = "Street address of the user", example = "Main Street")
    private String address;

    @Schema(description = "House or building number", example = "123")
    private String houseNumber;

    @Schema(description = "Postal or ZIP code", example = "00100")
    private String zipCode;

    @Schema(description = "City of residence", example = "Rome")
    private String city;

    @Schema(description = "Province or state abbreviation", example = "RM")
    private String province;
}
