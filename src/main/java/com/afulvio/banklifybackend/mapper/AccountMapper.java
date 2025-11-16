package com.afulvio.banklifybackend.mapper;

import com.afulvio.banklifybackend.model.dto.BalanceDTO;
import com.afulvio.banklifybackend.model.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    BalanceDTO toBalanceDTO(AccountEntity account);

}
