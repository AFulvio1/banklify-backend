package com.afulvio.banklifybackend.mapper;

import com.afulvio.banklifybackend.model.dto.MovementDTO;
import com.afulvio.banklifybackend.model.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "counterpartyName", source = "transaction", qualifiedByName = "getCounterpartyName")
    MovementDTO toTransactionDTO(TransactionEntity transaction);

    @Named("getCounterpartyName")
    default String getCounterpartyName(TransactionEntity transaction) {
        if (transaction.getTransactionType().name().startsWith("INCOMING")) {
            return "Da: " + transaction.getSenderName();
        } else {
            return "A: " + transaction.getReceiverName();
        }
    }

}
