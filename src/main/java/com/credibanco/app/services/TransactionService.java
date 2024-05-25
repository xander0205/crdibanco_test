package com.credibanco.app.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credibanco.app.entities.Transaction;
import com.credibanco.app.entities.Card;
import com.credibanco.app.repositories.CardRepository;
import com.credibanco.app.repositories.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CardRepository cardRepository;

    public Transaction purchase(String cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        if (!card.isActive() || card.isBlocked() || card.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Invalid transaction");
        }
        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);
        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setCancelled(false);
        return transactionRepository.save(transaction);
    }

    public Transaction cancelTransaction(String cardId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (transaction.isCancelled() || Duration.between(transaction.getTimestamp(), LocalDateTime.now()).toHours() > 24) {
            throw new RuntimeException("Invalid transaction cancellation");
        }
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        card.setBalance(card.getBalance().add(transaction.getAmount()));
        transaction.setCancelled(true);
        transactionRepository.save(transaction);
        cardRepository.save(card);
        return transaction;
    }

    public Transaction getTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}

