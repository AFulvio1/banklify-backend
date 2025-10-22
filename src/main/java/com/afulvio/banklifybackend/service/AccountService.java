package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.model.dto.BalanceDTO;
import com.afulvio.banklifybackend.model.entity.AccountEntity;
import com.afulvio.banklifybackend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public BalanceDTO getBalance(String iban) throws AccountNotFoundException {
        AccountEntity account = accountRepository.findById(iban)
                .orElseThrow(() -> new AccountNotFoundException("Conto non trovato per IBAN: " + iban));

        // Mappatura da Entity a DTO
        return BalanceDTO.builder()
                .iban(account.getIban())
                .ledgerBalance(account.getLedgerBalance())
                .availableBalance(account.getAvailableBalance())
                .build();
    }
}
