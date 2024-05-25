package com.credibanco.app.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.app.entities.Transaction;
import com.credibanco.app.services.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<Transaction> purchase(@RequestBody Map<String, Object> payload) {
        String cardId = (String) payload.get("cardId");
        BigDecimal amount = new BigDecimal((String) payload.get("price"));
        return ResponseEntity.ok(transactionService.purchase(cardId, amount));
    }

    @PostMapping("/anulation")
    public ResponseEntity<Transaction> cancelTransaction(@RequestBody Map<String, Object> payload) {
        String cardId = (String) payload.get("cardId");
        Long transactionId = Long.valueOf((String) payload.get("transactionId"));
        return ResponseEntity.ok(transactionService.cancelTransaction(cardId, transactionId));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.ok(transactionService.getTransaction(transactionId));
    }
}
