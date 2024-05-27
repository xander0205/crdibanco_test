package com.credibanco.app.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.credibanco.app.controller.TransactionController;
import com.credibanco.app.dtos.CancelTransactionDTO;
import com.credibanco.app.dtos.PurchaseDTO;
import com.credibanco.app.dtos.TransactionDTO;
import com.credibanco.app.services.ITransactionService;

class TransactionControllerTest {

    @Mock
    private ITransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPurchase_Success() {
        String cardId = "1234567890";
        BigDecimal amount = BigDecimal.valueOf(100);
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setCardId(cardId);
        purchaseDTO.setPrice(amount);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(1L);
        transactionDTO.setAmount(amount);

        when(transactionService.purchase(cardId, amount)).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.purchase(purchaseDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getTransactionId());
        assertEquals(amount, response.getBody().getAmount());

        verify(transactionService, times(1)).purchase(cardId, amount);
    }

    @Test
    void testPurchase_Failure() {
        String cardId = "1234567890";
        BigDecimal amount = BigDecimal.valueOf(100);
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setCardId(cardId);
        purchaseDTO.setPrice(amount);

        when(transactionService.purchase(cardId, amount)).thenThrow(new RuntimeException("Card is not active"));

        ResponseEntity<TransactionDTO> response = transactionController.purchase(purchaseDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(transactionService, times(1)).purchase(cardId, amount);
    }

    @Test
    void testCancelTransaction_Success() {
        String cardId = "1234567890";
        Long transactionId = 1L;
        CancelTransactionDTO cancelTransactionDTO = new CancelTransactionDTO();
        cancelTransactionDTO.setCardId(cardId);
        cancelTransactionDTO.setTransactionId(transactionId);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transactionId);
        transactionDTO.setCancelled(true);

        when(transactionService.cancelTransaction(cardId, transactionId)).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.cancelTransaction(cancelTransactionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transactionId, response.getBody().getTransactionId());
        assertTrue(response.getBody().isCancelled());

        verify(transactionService, times(1)).cancelTransaction(cardId, transactionId);
    }

    @Test
    void testCancelTransaction_Failure() {
        String cardId = "1234567890";
        Long transactionId = 1L;
        CancelTransactionDTO cancelTransactionDTO = new CancelTransactionDTO();
        cancelTransactionDTO.setCardId(cardId);
        cancelTransactionDTO.setTransactionId(transactionId);

        when(transactionService.cancelTransaction(cardId, transactionId)).thenThrow(new RuntimeException("Invalid cancellation"));

        ResponseEntity<TransactionDTO> response = transactionController.cancelTransaction(cancelTransactionDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(transactionService, times(1)).cancelTransaction(cardId, transactionId);
    }

    @Test
    void testGetTransaction_Success() {
        Long transactionId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transactionId);
        transactionDTO.setAmount(BigDecimal.valueOf(100));

        when(transactionService.getTransaction(transactionId)).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.getTransaction(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transactionId, response.getBody().getTransactionId());
        assertEquals(BigDecimal.valueOf(100), response.getBody().getAmount());

        verify(transactionService, times(1)).getTransaction(transactionId);
    }

    @Test
    void testGetTransaction_NotFound() {
        Long transactionId = 1L;

        when(transactionService.getTransaction(transactionId)).thenThrow(new RuntimeException("Transaction not found"));

        ResponseEntity<TransactionDTO> response = transactionController.getTransaction(transactionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(transactionService, times(1)).getTransaction(transactionId);
    }
}
