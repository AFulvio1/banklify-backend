package com.afulvio.banklifybackend.mapper;

import com.afulvio.banklifybackend.model.dto.MovementDTO;
import com.afulvio.banklifybackend.model.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    MovementDTO toTransactionDTO(TransactionEntity transaction);
}
