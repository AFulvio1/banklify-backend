package com.afulvio.banklifybackend.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "RegisterRequest",
        description = "Request body for user registration. All fields are required to create a new user account."
)
public class RegisterRequest {

    @Schema(
            description = "User's first name",
            example = "Mario",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String firstName;

    @Schema(
            description = "User's last name",
            example = "Rossi",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String lastName;

    @Schema(
            description = "User's Italian Tax Code (Codice Fiscale)",
            example = "MRORSS00A0A0001A",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String taxCode;

    @Schema(
            description = "User's email address (must be unique)",
            example = "mario.rossi@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email
    private String email;

    @Schema(
            description = "User's password (min 8 characters, strong password recommended)",
            example = "MyStrongP@ssw0rd!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String password;

}
