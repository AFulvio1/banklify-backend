package com.afulvio.banklifybackend.repository;

import com.afulvio.banklifybackend.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    List<AccountEntity> findByClientId(Long clientId);

    Optional<AccountEntity> findByIbanAndStatus(String iban, String status);

}
