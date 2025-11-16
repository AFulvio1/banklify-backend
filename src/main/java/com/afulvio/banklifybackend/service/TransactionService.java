package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.exception.InsufficientFundsException;
import com.afulvio.banklifybackend.mapper.TransactionMapper;
import com.afulvio.banklifybackend.model.dto.MovementDTO;
import com.afulvio.banklifybackend.model.dto.TransferDTO;
import com.afulvio.banklifybackend.model.entity.AccountEntity;
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
            throw new IllegalArgumentException("Mittente e Destinatario non possono essere lo stesso conto.");
        }

        AccountEntity sender = accountRepository.findByIbanAndStatus(senderIban, "ACTIVE")
                .orElseThrow(() -> new AccountNotFoundException("Conto mittente non trovato o inattivo."));

        AccountEntity receiver = accountRepository.findByIbanAndStatus(receiverIban, "ACTIVE")
                .orElseThrow(() -> new AccountNotFoundException("Conto destinatario non trovato o inattivo."));

        if (sender.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondi insufficienti per l'operazione.");
        }

        sender.setLedgerBalance(sender.getLedgerBalance().subtract(amount));
        sender.setAvailableBalance(sender.getAvailableBalance().subtract(amount));
        accountRepository.save(sender);

        receiver.setLedgerBalance(receiver.getLedgerBalance().add(amount));
        receiver.setAvailableBalance(receiver.getAvailableBalance().add(amount));
        accountRepository.save(receiver);

        LocalDateTime now = LocalDateTime.now();

        TransactionEntity debitTransaction = new TransactionEntity();
        debitTransaction.setAccountIban(senderIban);
        debitTransaction.setAmount(amount.negate());
        debitTransaction.setTransactionType("OUTGOING_TRANSFER");
        debitTransaction.setDescription("Bonifico a: " + receiverIban + " - Causale: " + transferDetails.getDescription());
        debitTransaction.setEventTimestamp(now);
        transactionRepository.save(debitTransaction);

        TransactionEntity creditTransaction = new TransactionEntity();
        creditTransaction.setAccountIban(receiverIban);
        creditTransaction.setAmount(amount);
        creditTransaction.setTransactionType("INCOMING_TRANSFER");
        creditTransaction.setDescription("Bonifico da: " + senderIban + " - Causale: " + transferDetails.getDescription());
        creditTransaction.setEventTimestamp(now);
        transactionRepository.save(creditTransaction);
    }

    public List<MovementDTO> getLatestMovements(String iban, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        return transactionRepository.findByAccountIbanOrderByEventTimestampDesc(iban, pageRequest).stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

}