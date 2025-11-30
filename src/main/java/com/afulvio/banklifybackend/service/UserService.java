package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.exception.ClientNotFoundException;
import com.afulvio.banklifybackend.model.entity.ClientEntity;
import com.afulvio.banklifybackend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ClientEntity client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("error.user.not.found"));
        return new User(client.getEmail(), client.getPasswordHash(), Collections.emptyList());
    }

}