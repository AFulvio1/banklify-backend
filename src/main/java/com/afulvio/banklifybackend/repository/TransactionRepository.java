package com.afulvio.banklifybackend.repository;

import com.afulvio.banklifybackend.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByAccountIbanOrderByTimestampDesc(String iban, Pageable pageable);

    List<TransactionEntity> findByAccountIbanOrderByTimestampDesc(String iban);

}
