package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.mapper.AccountMapper;
import com.afulvio.banklifybackend.model.dto.BalanceDTO;
import com.afulvio.banklifybackend.model.entity.AccountEntity;
import com.afulvio.banklifybackend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    public BalanceDTO getBalance(String iban) throws AccountNotFoundException {
        log.info("Getting balance for iban: {}", iban);
        AccountEntity account = accountRepository.findById(iban)
                .orElseThrow(() -> new AccountNotFoundException("error.account.iban.not.found"));
        log.info("Balance founded: {}", account);
        return accountMapper.toBalanceDTO(account);
    }
}
