package com.afulvio.banklifybackend.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "LoginResponse",
        description = "Response body after successful user authentication, containing the JWT token and user details."
)
public class LoginResponse {

    @Schema(
            name = "LoginResponse",
            description = "Response body after successful user authentication, containing the JWT token and user details."
    )
    private String token;

    @Schema(
            description = "The primary International Bank Account Number (IBAN) associated with the authenticated user.",
            example = "IT60X0542811101000000123456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String iban;

    @Schema(
            description = "The first name of the authenticated user.",
            example = "Mario",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

}