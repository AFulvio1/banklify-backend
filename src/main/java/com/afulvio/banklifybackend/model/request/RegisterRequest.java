package com.afulvio.banklifybackend.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "RegisterRequest",
        description = "Request body for user registration. All fields are required to create a new user account."
)
public class RegisterRequest {

    @Schema(description = "User's first name", example = "Mario")
    @NotBlank(message = "{error.validation.required}")
    private String firstName;

    @Schema(description = "User's last name", example = "Rossi")
    @NotBlank(message = "{error.validation.required}")
    private String lastName;

    @Schema(description = "User's email address (must be unique)", example = "mario.rossi@example.com")
    @NotBlank(message = "{error.validation.required}")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "User's password (min 8 characters)", example = "StrongP@ssw0rd!")
    @NotBlank(message = "{error.validation.required}")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Schema(description = "Italian Tax Code (Codice Fiscale)", example = "RSSMRA80A01H501U")
    @NotBlank(message = "{error.validation.required}")
    @Size(min = 16, max = 16, message = "Tax code must be exactly 16 characters")
    private String taxCode;

    @Schema(description = "Street name", example = "Via Roma")
    @NotBlank(message = "{error.validation.required}")
    private String street;

    @Schema(description = "House number", example = "10")
    @NotBlank(message = "{error.validation.required}")
    private String houseNumber;

    @Schema(description = "City of residence", example = "Milano")
    @NotBlank(message = "{error.validation.required}")
    private String city;

    @Schema(description = "Province abbreviation (2 chars)", example = "MI")
    @NotBlank(message = "{error.validation.required}")
    @Size(min = 2, max = 2, message = "Province must be exactly 2 characters")
    private String province;

    @Schema(description = "Postal/Zip code", example = "20121")
    @NotBlank(message = "{error.validation.required}")
    @Size(min = 5, max = 5, message = "Zip code must be exactly 5 digits")
    private String zipCode;

    @Schema(description = "User's phone number", example = "+393331234567")
    @NotBlank(message = "{error.validation.required}")
    private String phoneNumber;

}
