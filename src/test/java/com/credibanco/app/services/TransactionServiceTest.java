package com.credibanco.app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import com.credibanco.app.dtos.TransactionDTO;
import com.credibanco.app.entities.Card;
import com.credibanco.app.entities.Transaction;
import com.credibanco.app.repositories.CardRepository;
import com.credibanco.app.repositories.TransactionRepository;
import com.credibanco.app.exceptions.MessageNotFoundException;

class TransactionServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Environment messages;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPurchase() {
        String cardId = "1234567890";
        BigDecimal amount = BigDecimal.valueOf(100);
        Card card = new Card();
        card.setCardId(cardId);
        card.setActive(true);
        card.setBlocked(false);
        card.setBalance(BigDecimal.valueOf(200));
        card.setExpirationDate(LocalDate.now().plusYears(1));

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setTransactionId(1L); 
            return transaction;
        });

        TransactionDTO transactionDTO = transactionService.purchase(cardId, amount);

        assertNotNull(transactionDTO);
        assertEquals(1L, transactionDTO.getTransactionId());
        assertEquals(amount, transactionDTO.getAmount());
        verify(cardRepository, times(1)).findById(cardId);
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testPurchase_CardNotActive() {
        String cardId = "1234567890";
        BigDecimal amount = BigDecimal.valueOf(100);
        Card card = new Card();
        card.setCardId(cardId);
        card.setActive(false);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(messages.getProperty("message.response.transaction.cardNotActive")).thenReturn("Card is not active");

        MessageNotFoundException exception = assertThrows(MessageNotFoundException.class, () -> {
            transactionService.purchase(cardId, amount);
        });

        assertEquals("Card is not active", exception.getMessage());
        verify(cardRepository, times(1)).findById(cardId);
    }

    @Test
    void testCancelTransaction() {
        String cardId = "1234567890";
        Long transactionId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(BigDecimal.ZERO);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setCard(card);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setCancelled(false);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionDTO transactionDTO = transactionService.cancelTransaction(cardId, transactionId);

        assertNotNull(transactionDTO);
        assertEquals(transactionId, transactionDTO.getTransactionId());
        assertTrue(transactionDTO.isCancelled());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(cardRepository, times(1)).findById(cardId);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void testGetTransaction() {
        Long transactionId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        TransactionDTO transactionDTO = transactionService.getTransaction(transactionId);

        assertNotNull(transactionDTO);
        assertEquals(transactionId, transactionDTO.getTransactionId());
        assertEquals(amount, transactionDTO.getAmount());
        verify(transactionRepository, times(1)).findById(transactionId);
    }
}
