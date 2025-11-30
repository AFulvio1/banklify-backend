package com.afulvio.banklifybackend.mapper;

import com.afulvio.banklifybackend.model.dto.BalanceDTO;
import com.afulvio.banklifybackend.model.entity.AccountEntity;
import com.afulvio.banklifybackend.model.entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    BalanceDTO toBalanceDTO(AccountEntity account);

    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "openingDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ledgerBalance", constant = "0")
    @Mapping(target = "availableBalance", constant = "0")
    AccountEntity newAccount(ClientEntity client, String iban);

}
