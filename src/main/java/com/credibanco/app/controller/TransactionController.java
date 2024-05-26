package com.credibanco.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.app.dtos.CancelTransactionDTO;
import com.credibanco.app.dtos.PurchaseDTO;
import com.credibanco.app.dtos.TransactionDTO;
import com.credibanco.app.services.ITransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<TransactionDTO> purchase(@RequestBody PurchaseDTO dto) {
        try {
            return ResponseEntity.ok(transactionService.purchase(dto.getCardId(), dto.getPrice()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/anulation")
    public ResponseEntity<TransactionDTO> cancelTransaction(@RequestBody CancelTransactionDTO dto) {
        try {
            TransactionDTO transactionDTO = transactionService.cancelTransaction(dto.getCardId(), dto.getTransactionId());
            return ResponseEntity.ok(transactionDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long transactionId) {
        try {
            TransactionDTO transactionDTO = transactionService.getTransaction(transactionId);
            return ResponseEntity.ok(transactionDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}