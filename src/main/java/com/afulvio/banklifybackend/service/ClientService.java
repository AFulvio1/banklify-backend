package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.model.entity.ClientEntity;
import com.afulvio.banklifybackend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ClientService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ClientEntity client = clientRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("user '" + email + "' not found"));
        return new org.springframework.security.core.userdetails.User(
                client.getEmail(),
                client.getPasswordHash(),
                Collections.emptyList()
        );
    }
}
