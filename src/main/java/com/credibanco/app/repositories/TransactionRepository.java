package com.credibanco.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.credibanco.app.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCard_CardId(String cardId);
}

