package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.configuration.JwtTokenProvider;
import com.afulvio.banklifybackend.model.entity.ClientEntity;
import com.afulvio.banklifybackend.model.request.LoginRequest;
import com.afulvio.banklifybackend.model.request.RegisterRequest;
import com.afulvio.banklifybackend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository clientRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    public void registerNewUser(RegisterRequest request) {

        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("The email is already in use.");
        }

        ClientEntity newUser = new ClientEntity();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setTaxCode(request.getTaxCode());
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        clientRepository.save(newUser);
    }

    public String authenticateAndGenerateToken(LoginRequest request) {

        Optional<ClientEntity> clientOpt = clientRepository.findByEmail(request.getEmail());

        if (clientOpt.isEmpty()) {
            throw new RuntimeException("Credential not valid.");
        }

        ClientEntity client = clientOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), client.getPasswordHash())) {
            throw new RuntimeException("Credential not valid.");
        }

        return jwtTokenProvider.generateToken(client.getEmail(), client.getClientId());
    }

}
