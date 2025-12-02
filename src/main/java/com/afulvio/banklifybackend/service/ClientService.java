package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.exception.ClientNotFoundException;
import com.afulvio.banklifybackend.mapper.ClientMapper;
import com.afulvio.banklifybackend.model.dto.UserProfileDTO;
import com.afulvio.banklifybackend.model.entity.ClientEntity;
import com.afulvio.banklifybackend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public UserProfileDTO getCurrentUserProfile() {
        log.info("Getting current user profile");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ClientEntity client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("error.user.not.found"));
        log.info("Current user profile: {}", client.getEmail());
        return clientMapper.toUserProfile(client);
    }

}
