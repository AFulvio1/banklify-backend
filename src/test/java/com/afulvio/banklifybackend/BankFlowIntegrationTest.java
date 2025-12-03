package com.afulvio.banklifybackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BankFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String authHeader;
    private static String iban;

    private String baseRegisterJson() {
        return """
            {
              "firstName": "Mario",
              "lastName": "Rossi",
              "email": "mario.rossi@example.com",
              "password": "Password123!",
              "taxCode": "RSSMRA80A01H501U",
              "address": "Via Roma",
              "houseNumber": "10",
              "city": "Milano",
              "province": "MI",
              "zipCode": "20121",
              "phoneNumber": "+393331234567",
              "birthDate": "1980-01-01"
            }
            """;
    }

    @Test
    @Order(10)
    @DisplayName("POST /api/v1/auth/register - OK")
    void register() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(baseRegisterJson()))
                .andExpect(status().isCreated())
                .andExpect(content().string("Registration successfully done"));
    }

    @Test
    @Order(11)
    @DisplayName("POST /api/v1/auth/register - Bad Request, Missing All Required Fields")
    void register_missingAllRequiredFields() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(body);
        String errorString = node.get("error").asText();

        String[] errors = errorString.split(", ");

        assertThat(errors).containsExactlyInAnyOrder(
                "Il campo firstName e obbligatorio",
                "Il campo lastName e obbligatorio",
                "Il campo email e obbligatorio",
                "Il campo password e obbligatorio",
                "Il campo taxCode e obbligatorio",
                "Il campo address e obbligatorio",
                "Il campo houseNumber e obbligatorio",
                "Il campo city e obbligatorio",
                "Il campo province e obbligatorio",
                "Il campo zipCode e obbligatorio",
                "Il campo phoneNumber e obbligatorio",
                "Il campo birthDate e obbligatorio"
        );
    }

    @Test
    @Order(12)
    @DisplayName("POST /api/v1/auth/register - Bad Request Invalid Email Format")
    void register_invalidEmail() throws Exception {
        String body = baseRegisterJson().replace("mario.rossi@example.com", "not-an-email");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid email format"));
    }

    @Test
    @Order(13)
    @DisplayName("POST /api/v1/auth/register - Bad Request Short Password")
    void register_shortPassword() throws Exception {
        String body = baseRegisterJson().replace("Password123!", "12345");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Password must be at least 8 characters long"));
    }

    @Test
    @Order(14)
    @DisplayName("POST /api/v1/auth/register - Bad Request Invalid Tax Code Length")
    void register_invalidTaxCodeLength() throws Exception {
        String body = baseRegisterJson().replace("RSSMRA80A01H501U", "CFTROPPOCORTO");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Tax code must be exactly 16 characters"));
    }

    @Test
    @Order(15)
    @DisplayName("POST /api/v1/auth/register - Bad Request Invalid Zip Code Length")
    void register_invalidProvinceLength() throws Exception {
        String body = baseRegisterJson().replace("\"province\": \"MI\"", "\"province\": \"MIL\"");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Province must be exactly 2 characters"));
    }

    @Test
    @Order(16)
    @DisplayName("POST /api/v1/auth/register - Bad Request Invalid Zip Code Length")
    void register_invalidZipCodeLength() throws Exception {
        String body = baseRegisterJson().replace("\"zipCode\": \"20121\"", "\"zipCode\": \"2012\"");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Zip code must be exactly 5 digits"));
    }

    @Test
    @Order(17)
    @DisplayName("POST /api/v1/auth/register - Bad Request Null Birth Date")
    void register_nullBirthDate() throws Exception {
        String body = baseRegisterJson().replace("\"birthDate\": \"1980-01-01\"", "\"birthDate\": null");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Il campo birthDate e obbligatorio"));
    }

//    @Test
//    @Order(18)
//    @DisplayName("POST /api/v1/auth/register - Conflict Email Already Used")
//    void register_emailAlreadyUsed() throws Exception {
//        String body = baseRegisterJson().replace("mario.rossi@example.com", "duplicate@example.com");
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("Registration successfully done"));
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isConflict())
//                .andExpect(jsonPath("$.message").value("error.auth.email.in.use"));
//    }

    @Test
    @Order(20)
    @DisplayName(("POST /api/v1/auth/login"))
    void logIn() throws Exception {
        String loginJson = """
            {
              "email": "mario.rossi@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.iban").exists())
                .andExpect(jsonPath("$.firstName").value("Mario"))
                .andReturn();

        String loginResponseBody = loginResult.getResponse().getContentAsString();
        JsonNode loginNode = objectMapper.readTree(loginResponseBody);
        String jwtToken = loginNode.get("token").asText();
        iban = loginNode.get("iban").asText();

        assertThat(jwtToken).isNotBlank();
        assertThat(iban).isNotBlank();

        authHeader = "Bearer " + jwtToken;
    }

    @Test
    @Order(30)
    @DisplayName(("GET /api/v1/client/profile"))
    void profile() throws Exception {
        mockMvc.perform(get("/api/v1/client/profile")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mario"))
                .andExpect(jsonPath("$.email").value("mario.rossi@example.com"));
    }

    @Test
    @Order(40)
    @DisplayName(("POST /api/v1/transactions/external/transfer - IN"))
    void externalTransferIn() throws Exception {
        String externalInTransferJson = """
            {
              "senderName": "Azienda Spa",
              "senderIban": "EXT-BANK-IBAN-123",
              "receiverName": "Mario Rossi",
              "receiverIban": "%s",
              "amount": 1000.00,
              "currency": "EUR",
              "description": "Accredito stipendio"
            }
            """.formatted(iban);

        mockMvc.perform(post("/api/v1/transactions/external/transfer")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(externalInTransferJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successfully completed"));
    }

    @Test
    @Order(41)
    @DisplayName(("POST /api/v1/transactions/external/transfer - OUT"))
    void externalTransferOut() throws Exception {
        String externalOutTransferJson = """
            {
              "senderName": "Mario Rossi",
              "senderIban": "%s",
              "receiverName": "Luigi Bianchi",
              "receiverIban": "IT99H0123456789000000009999",
              "amount": 200.00,
              "currency": "EUR",
              "description": "Bonifico affitto"
            }
            """.formatted(iban);

        mockMvc.perform(post("/api/v1/transactions/external/transfer")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(externalOutTransferJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successfully completed"));
    }

    @Test
    @Order(50)
    @DisplayName(("GET /api/v1/transactions/{iban}/movements"))
    void listMovements() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/{iban}/movements", iban)
                        .header("Authorization", authHeader)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                // ci aspettiamo almeno i 2 movimenti generati sopra
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)));
    }

}
