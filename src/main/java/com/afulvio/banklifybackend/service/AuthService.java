package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.model.entity.AccountEntity;
import com.afulvio.banklifybackend.model.entity.ClientEntity;
import com.afulvio.banklifybackend.model.request.LoginRequest;
import com.afulvio.banklifybackend.model.request.RegisterRequest;
import com.afulvio.banklifybackend.model.response.LoginResponse;
import com.afulvio.banklifybackend.repository.AccountRepository;
import com.afulvio.banklifybackend.repository.ClientRepository;
import com.afulvio.banklifybackend.security.JwtTokenProvider;
import com.afulvio.banklifybackend.util.IbanGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void registerNewUser(RegisterRequest request) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("The email is already in use.");
        }

        ClientEntity newUser = new ClientEntity();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setTaxCode(request.getTaxCode());
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        ClientEntity savedUser = clientRepository.save(newUser);

        AccountEntity newAccount = new AccountEntity();
        newAccount.setIban(IbanGenerator.generateUniqueIban(savedUser.getClientId()));
        newAccount.setClient(savedUser);
        newAccount.setLedgerBalance(BigDecimal.ZERO);
        newAccount.setAvailableBalance(BigDecimal.ZERO);
        newAccount.setStatus("ACTIVE");
        accountRepository.save(newAccount);
    }

    public LoginResponse authenticateAndGenerateToken(LoginRequest request) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Optional<ClientEntity> clientOpt = clientRepository.findByEmail(request.getEmail());

        if (clientOpt.isEmpty()) {
            throw new RuntimeException("Credential not valid.");
        }

        ClientEntity client = clientOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), client.getPasswordHash())) {
            throw new RuntimeException("Credential not valid.");
        }

        String token = jwtTokenProvider.generateToken(client.getEmail(), client.getClientId());
        String iban = accountRepository.findByClientClientId(client.getClientId()).stream()
                .findFirst()
                .map(AccountEntity::getIban)
                .orElseThrow(() -> new RuntimeException("Primary account not found."));

        return new LoginResponse(token, iban, client.getFirstName());
    }

}
