package com.afulvio.banklifybackend.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "LoginRequest",
        description = "Request body for user authentication (login). Both fields are required."
)
public class LoginRequest {

    @Schema(
            description = "User's email address",
            example = "mario.rossi@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "User's password",
            example = "MyStrongP@ssw0rd!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

}
