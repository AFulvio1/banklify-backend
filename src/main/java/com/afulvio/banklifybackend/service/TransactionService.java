package com.afulvio.banklifybackend.service;

import com.afulvio.banklifybackend.exception.InsufficientFundsException;
import com.afulvio.banklifybackend.model.dto.TransferDTO;
import com.afulvio.banklifybackend.model.entity.AccountEntity;
import com.afulvio.banklifybackend.model.entity.TransactionEntity;
import com.afulvio.banklifybackend.repository.AccountRepository;
import com.afulvio.banklifybackend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    // Questa annotazione è CRITICA: garantisce che se una parte fallisce, tutto viene annullato
    @Transactional
    public void executeTransfer(TransferDTO transferDetails) throws AccountNotFoundException, InsufficientFundsException {

        String senderIban = transferDetails.getSenderIban();
        String receiverIban = transferDetails.getReceiverIban();
        BigDecimal amount = transferDetails.getAmount();

        if (senderIban.equals(receiverIban)) {
            throw new IllegalArgumentException("Mittente e Destinatario non possono essere lo stesso conto.");
        }

        // 1. Trova e blocca i conti (in JPA l'utilizzo di findById in @Transactional aiuta)
        AccountEntity sender = accountRepository.findByIbanAndStatus(senderIban, "ACTIVE")
                .orElseThrow(() -> new AccountNotFoundException("Conto mittente non trovato o inattivo."));

        AccountEntity receiver = accountRepository.findByIbanAndStatus(receiverIban, "ACTIVE")
                .orElseThrow(() -> new AccountNotFoundException("Conto destinatario non trovato o inattivo."));

        // 2. Verifica fondi disponibili
        if (sender.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondi insufficienti per l'operazione.");
        }

        // 3. Addebito (Mittente)
        sender.setLedgerBalance(sender.getLedgerBalance().subtract(amount));
        sender.setAvailableBalance(sender.getAvailableBalance().subtract(amount));
        accountRepository.save(sender);

        // 4. Accredito (Destinatario)
        receiver.setLedgerBalance(receiver.getLedgerBalance().add(amount));
        receiver.setAvailableBalance(receiver.getAvailableBalance().add(amount));
        accountRepository.save(receiver);

        LocalDateTime now = LocalDateTime.now();

        // 5. Registrazione Movimento (Addebito)
        TransactionEntity debitTransaction = new TransactionEntity();
        debitTransaction.setAccountIban(senderIban);
        debitTransaction.setAmount(amount.negate()); // Importo negativo
        debitTransaction.setTransactionType("OUTGOING_TRANSFER");
        debitTransaction.setDescription("Bonifico a: " + receiverIban + " - Causale: " + transferDetails.getDescription());
        debitTransaction.setTimestamp(now);
        transactionRepository.save(debitTransaction);

        // 6. Registrazione Movimento (Accredito)
        TransactionEntity creditTransaction = new TransactionEntity();
        creditTransaction.setAccountIban(receiverIban);
        creditTransaction.setAmount(amount); // Importo positivo
        creditTransaction.setTransactionType("INCOMING_TRANSFER");
        creditTransaction.setDescription("Bonifico da: " + senderIban + " - Causale: " + transferDetails.getDescription());
        creditTransaction.setTimestamp(now);
        transactionRepository.save(creditTransaction);
    }
}