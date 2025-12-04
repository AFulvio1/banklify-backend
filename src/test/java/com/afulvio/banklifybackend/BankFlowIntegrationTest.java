package com.afulvio.banklifybackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
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

    private static String ibanMario;
    private static String ibanGiovanni;

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
    @DisplayName("POST /api/v1/auth/register - Bad Request: Missing All Required Fields")
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
                "This field firstName is required",
                "This field lastName is required",
                "This field email is required",
                "This field password is required",
                "This field taxCode is required",
                "This field address is required",
                "This field houseNumber is required",
                "This field city is required",
                "This field province is required",
                "This field zipCode is required",
                "This field phoneNumber is required",
                "This field birthDate is required"
        );
    }

    @Test
    @Order(12)
    @DisplayName("POST /api/v1/auth/register - Bad Request: Invalid Email Format")
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
    @DisplayName("POST /api/v1/auth/register - Bad Request: Short Password")
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
    @DisplayName("POST /api/v1/auth/register - Bad Request: Invalid Tax Code Length")
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
    @DisplayName("POST /api/v1/auth/register - Bad Request: Invalid Zip Code Length")
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
    @DisplayName("POST /api/v1/auth/register - Bad Request: Invalid Zip Code Length")
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
    @DisplayName("POST /api/v1/auth/register - Bad Request: Null Birth Date")
    void register_nullBirthDate() throws Exception {
        String body = baseRegisterJson().replace("\"birthDate\": \"1980-01-01\"", "\"birthDate\": null");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("This field birthDate is required"));
    }

    @Test
    @Order(18)
    @DisplayName("POST /api/v1/auth/register - Conflict: Email Already Used")
    void register_emailAlreadyUsed() throws Exception {
        String body = baseRegisterJson()
                .replace("mario.rossi@example.com", "giovanni.bianchi@example.com")
                .replace("RSSMRA80A01H501U", "BNCGNN80A01H501U");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string("Registration successfully done"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("The email is already in use"));
    }

    @Test
    @Order(20)
    @DisplayName(("POST /api/v1/auth/login - OK"))
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
        ibanMario = loginNode.get("iban").asText();

        assertThat(jwtToken).isNotBlank();
        assertThat(ibanMario).isNotBlank();

        authHeader = "Bearer " + jwtToken;
    }

    @Test
    @Order(21)
    @DisplayName(("POST /api/v1/auth/login - Unauthorized: Invalid password"))
    void logIn_invalidPassword() throws Exception {
        String loginJson = """
            {
              "email": "mario.rossi@example.com",
              "password": "WrongPassword!"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    @Order(22)
    @DisplayName(("POST /api/v1/auth/login - Not Found: Client Not Found"))
    void logIn_clientNotFound() throws Exception {
        String loginJson = """
            {
              "email": "user.not.exist@exemple.com",
              "password": "Password123!"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    @Order(23)
    @DisplayName("POST /api/v1/auth/login - OK: Second User Registration")
    void registerSecondUser() throws Exception {
        String loginJson = """
            {
              "email": "giovanni.bianchi@example.com",
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
        ibanGiovanni = loginNode.get("iban").asText();
    }

    @Test
    @Order(30)
    @DisplayName(("GET /api/v1/client/profile - OK"))
    void profile() throws Exception {
        mockMvc.perform(get("/api/v1/client/profile")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mario"))
                .andExpect(jsonPath("$.lastName").value("Rossi"))
                .andExpect(jsonPath("$.email").value("mario.rossi@example.com"));
    }

    @Test
    @Order(31)
    @DisplayName(("GET /api/v1/client/profile - Unauthorized: Missing Token"))
    void profile_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/client/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(40)
    @DisplayName(("POST /api/v1/transactions/external/transfer - OK: In"))
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
            """.formatted(ibanMario);

        mockMvc.perform(post("/api/v1/transactions/external/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(externalInTransferJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successfully completed"));
    }

    @Test
    @Order(41)
    @DisplayName(("POST /api/v1/transactions/external/transfer - OK: Out"))
    void externalTransferOut() throws Exception {
        String externalOutTransferJson = """
            {
              "senderName": "Mario Rossi",
              "senderIban": "%s",
              "receiverName": "Luigi Bianchi",
              "receiverIban": "IT99H0123456789000000009999",
              "amount": 317.33,
              "currency": "EUR",
              "description": "Bonifico affitto"
            }
            """.formatted(ibanMario);

        mockMvc.perform(post("/api/v1/transactions/external/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(externalOutTransferJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successfully completed"));
    }

    @Test
    @Order(42)
    @DisplayName(("POST /api/v1/transactions/transfer - OK"))
    void transferOut() throws Exception {
        String outTransferJson = """
            {
              "senderIban": "%s",
              "receiverName": "Giovanni Bianchi",
              "receiverIban": "%s",
              "amount": 600.00,
              "currency": "EUR",
              "description": "Regalo compleanno"
            }
            """.formatted(ibanMario, ibanGiovanni);

        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(outTransferJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successfully completed"));
    }

    @Test
    @Order(43)
    @DisplayName(("POST /api/v1/transactions/external/transfer - Bad Request: Missing Sender Name"))
    void externalTransferIn_missingSenderName() throws Exception {
        String externalInTransferJson = """
            {
              "senderIban": "EXT-BANK-IBAN-123",
              "receiverName": "Mario Rossi",
              "receiverIban": "%s",
              "amount": 1000.00,
              "currency": "EUR",
              "description": "Accredito stipendio"
            }
            """.formatted(ibanMario);

        mockMvc.perform(post("/api/v1/transactions/external/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(externalInTransferJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The sender name must be provided for external transfers"));
    }

    @Test
    @Order(44)
    @DisplayName(("POST /api/v1/transactions/transfer - Unauthorized: Missing Auth Header"))
    void transferOut_missingAuthHeader() throws Exception {
        String outTransferJson = """
            {
              "senderIban": "%s",
              "receiverName": "Giovanni Bianchi",
              "receiverIban": "%s",
              "amount": 200.00,
              "currency": "EUR",
              "description": "Regalo compleanno"
            }
            """.formatted(ibanMario, ibanGiovanni);

        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(outTransferJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(45)
    @DisplayName(("POST /api/v1/transactions/transfer - Bad Request: Same Account Transfer"))
    void transferOut_sameAccountTransfer() throws Exception {
        String outTransferJson = """
            {
              "senderIban": "%s",
              "receiverName": "Giovanni Bianchi",
              "receiverIban": "%s",
              "amount": 200.00,
              "currency": "EUR",
              "description": "Regalo compleanno"
            }
            """.formatted(ibanMario, ibanMario);

        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(outTransferJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Sender and Receiver cannot be the same account"));
    }

    @Test
    @Order(46)
    @DisplayName(("POST /api/v1/transactions/transfer - Bad Request: Account Not Found"))
    void transferOut_accountNotFound() throws Exception {
        String outTransferJson = """
            {
              "senderIban": "%s",
              "receiverName": "Giovanni Bianchi",
              "receiverIban": "%s",
              "amount": 200.00,
              "currency": "EUR",
              "description": "Regalo compleanno"
            }
            """.formatted("XXXXXX00X00X000X", "YYYYYY00Y00Y000Y");

        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(outTransferJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Neither sender nor receiver belongs to this bank"));
    }

    @Test
    @Order(47)
    @DisplayName(("POST /api/v1/transactions/transfer - Conflict: Insufficient Funds"))
    void transferOut_insufficientFund() throws Exception {
        String outTransferJson = """
            {
              "senderIban": "%s",
              "receiverName": "Giovanni Bianchi",
              "receiverIban": "%s",
              "amount": 100.00,
              "currency": "EUR",
              "description": "Regalo compleanno"
            }
            """.formatted(ibanMario, ibanGiovanni);

        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(outTransferJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Insufficient funds for this transaction"));
    }

    @Test
    @Order(50)
    @DisplayName(("GET /api/v1/transactions/{iban}/movements - OK"))
    void listMovements() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/{iban}/movements", ibanMario)
                        .header("Authorization", authHeader)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)));
    }

    @Test
    @Order(51)
    @DisplayName(("GET /api/v1/transactions/{iban}/movements - Unauthorized: Missing Auth Header"))
    void listMovements_missingAuthHeader() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/{iban}/movements", ibanMario)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(60)
    @DisplayName(("GET /api/v1/accounts/{iban}/balance - OK"))
    void balance() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{iban}/balance", ibanMario)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ledgerBalance").value(82.67))
                .andExpect(jsonPath("$.availableBalance").value(82.67));
    }

    @Test
    @Order(61)
    @DisplayName(("GET /api/v1/accounts/{iban}/balance - Unauthorized: Missing Auth Header"))
    void balance_missingAuthHeader() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{iban}/balance", ibanMario))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(62)
    @DisplayName(("GET /api/v1/accounts/{iban}/balance - Not Found: Invalid IBAN"))
    void balance_invalidIban() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{iban}/balance", "XXXXXX00X00X000X")
                        .header("Authorization", authHeader))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No account found for the specified IBAN"));
    }

}
