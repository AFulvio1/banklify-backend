package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.exception.AccountNotFoundException;
import com.afulvio.banklifybackend.exception.ClientNotFoundException;
import com.afulvio.banklifybackend.exception.EmailAlreadyRegisteredException;
import com.afulvio.banklifybackend.exception.InvalidCredentialException;
import com.afulvio.banklifybackend.mapper.AccountMapper;
import com.afulvio.banklifybackend.mapper.ClientMapper;
import com.afulvio.banklifybackend.model.dto.UserProfileDTO;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewUser(RegisterRequest request) {

        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("error.auth.email.in.use");
        }

        ClientEntity newUser = clientMapper.toClientEntity(request, passwordEncoder.encode(request.getPassword()));
        ClientEntity savedUser = clientRepository.save(newUser);

        String iban = IbanGenerator.generateUniqueIban(savedUser.getClientId());
        AccountEntity newAccount = accountMapper.newAccount(savedUser, iban);
        accountRepository.save(newAccount);
    }

    public LoginResponse authenticateAndGenerateToken(LoginRequest request) {

        ClientEntity client = clientRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ClientNotFoundException("error.auth.invalid.credentials"));

        if (!passwordEncoder.matches(request.getPassword(), client.getPasswordHash())) {
            throw new InvalidCredentialException("error.auth.invalid.credentials");
        }

        String token = jwtTokenProvider.generateToken(client.getEmail(), client.getClientId());

        String iban = accountRepository.findByClientClientId(client.getClientId()).stream()
                .findFirst()
                .map(AccountEntity::getIban)
                .orElseThrow(() -> new AccountNotFoundException("error.account.not.found"));

        return new LoginResponse(token, iban, client.getFirstName());
    }

    public UserProfileDTO getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ClientEntity client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("error.user.not.found"));
        return clientMapper.toUserProfile(client);
    }

}
