package com.credibanco.app.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credibanco.app.entities.Transaction;
import com.credibanco.app.dtos.TransactionDTO;
import com.credibanco.app.entities.Card;
import com.credibanco.app.repositories.CardRepository;
import com.credibanco.app.repositories.TransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CardRepository cardRepository;

    public TransactionDTO purchase(String cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));

        logger.info("Purchasing with cardId: {}, amount: {}", cardId, amount);

        if (!card.isActive()) {
            logger.error("Card is not active");
            throw new RuntimeException("Card is not active");
        }
        if (card.isBlocked()) {
            logger.error("Card is blocked");
            throw new RuntimeException("Card is blocked");
        }
        if (card.getBalance().compareTo(amount) < 0) {
            logger.error("Insufficient balance. Card balance: {}, Purchase amount: {}", card.getBalance(), amount);
            throw new RuntimeException("Insufficient balance");
        }
        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            logger.error("Card has expired. Expiration date: {}", card.getExpirationDate());
            throw new RuntimeException("Card has expired");
        }

        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setCancelled(false);
        transaction = transactionRepository.save(transaction);
        
        if (transaction == null) {
            throw new RuntimeException("Error saving transaction");
        }
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTimestamp(transaction.getTimestamp());

        return transactionDTO;
    }

    public TransactionDTO cancelTransaction(String cardId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (transaction.isCancelled() || Duration.between(transaction.getTimestamp(), LocalDateTime.now()).toHours() > 24) {
            throw new RuntimeException("Invalid transaction cancellation");
        }
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        card.setBalance(card.getBalance().add(transaction.getAmount()));
        transaction.setCancelled(true);
        transactionRepository.save(transaction);
        cardRepository.save(card);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTimestamp(transaction.getTimestamp());
        transactionDTO.setCancelled(transaction.isCancelled());

        return transactionDTO;
    }

    public TransactionDTO getTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTimestamp(transaction.getTimestamp());
        transactionDTO.setCancelled(transaction.isCancelled());
        
        return transactionDTO;
    }
}