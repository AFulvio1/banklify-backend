package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.exception.InsufficientFundsException;
import com.afulvio.banklifybackend.mapper.TransactionMapper;
import com.afulvio.banklifybackend.model.TransactionType;
import com.afulvio.banklifybackend.model.dto.MovementDTO;
import com.afulvio.banklifybackend.model.dto.TransferDTO;
import com.afulvio.banklifybackend.model.entity.AccountEntity;
import com.afulvio.banklifybackend.model.entity.ClientEntity;
import com.afulvio.banklifybackend.model.entity.TransactionEntity;
import com.afulvio.banklifybackend.repository.AccountRepository;
import com.afulvio.banklifybackend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    @Transactional
    public void executeTransfer(TransferDTO transferDetails) throws AccountNotFoundException, InsufficientFundsException {

        String senderIban = transferDetails.getSenderIban();
        String receiverIban = transferDetails.getReceiverIban();
        BigDecimal amount = transferDetails.getAmount();

        if (senderIban.equals(receiverIban)) {
            throw new IllegalArgumentException("error.transfer.same.account");
        }

        Optional<AccountEntity> senderOpt = accountRepository.findByIbanAndStatus(senderIban, "ACTIVE");
        Optional<AccountEntity> receiverOpt = accountRepository.findByIbanAndStatus(receiverIban, "ACTIVE");

        if (senderOpt.isEmpty() && receiverOpt.isEmpty()) {
            throw new AccountNotFoundException("error.transfer.parties.not.found");
        }

        LocalDateTime now = LocalDateTime.now();

        if (senderOpt.isPresent()) {
            AccountEntity sender = senderOpt.get();

            if (sender.getAvailableBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("error.insufficient.funds");
            }

            sender.setLedgerBalance(sender.getLedgerBalance().subtract(amount));
            sender.setAvailableBalance(sender.getAvailableBalance().subtract(amount));
            accountRepository.save(sender);

            TransactionEntity debitTransaction = new TransactionEntity();
            debitTransaction.setAccountIban(senderIban);
            debitTransaction.setAmount(amount.negate());
            debitTransaction.setTransactionType(receiverOpt.isPresent() ? TransactionType.OUTGOING_INTERNAL: TransactionType.OUTGOING_EXTERNAL);
            debitTransaction.setSenderIban(senderIban);
            debitTransaction.setSenderName(getFullName(sender.getClient()));
            debitTransaction.setReceiverIban(receiverIban);
            debitTransaction.setReceiverName(transferDetails.getReceiverName());
            debitTransaction.setDescription(transferDetails.getDescription());
            debitTransaction.setEventTimestamp(now);
            transactionRepository.save(debitTransaction);
        }


        if (receiverOpt.isPresent()) {
            AccountEntity receiver = receiverOpt.get();

            receiver.setLedgerBalance(receiver.getLedgerBalance().add(amount));
            receiver.setAvailableBalance(receiver.getAvailableBalance().add(amount));
            accountRepository.save(receiver);

            String senderName = senderOpt.map(accountEntity -> getFullName(accountEntity.getClient()))
                    .orElseGet(transferDetails::getSenderName);

            TransactionEntity creditTransaction = new TransactionEntity();
            creditTransaction.setAccountIban(receiverIban);
            creditTransaction.setAmount(amount);
            creditTransaction.setTransactionType(senderOpt.isPresent() ? TransactionType.INCOMING_INTERNAL: TransactionType.INCOMING_EXTERNAL);
            creditTransaction.setSenderIban(senderIban);
            creditTransaction.setSenderName(senderName);
            creditTransaction.setReceiverIban(receiverIban);
            creditTransaction.setReceiverName(getFullName(receiver.getClient()));
            creditTransaction.setDescription(transferDetails.getDescription());
            creditTransaction.setEventTimestamp(now);
            transactionRepository.save(creditTransaction);
        }
    }

    private String getFullName(ClientEntity client) {
        return client.getFirstName() + " " + client.getLastName();
    }


    public List<MovementDTO> getLatestMovements(String iban, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return transactionRepository.findByAccountIbanOrderByEventTimestampDesc(iban, pageRequest).stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

}