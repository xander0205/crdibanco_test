package com.credibanco.app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.credibanco.app.entities.Card;
import com.credibanco.app.repositories.CardRepository;
import com.credibanco.app.services.CardService;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCardNumber() {
        String productId = "123456";
        String holderName = "CrediBanCo";
        String expectedCardPrefix = productId;

        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String cardNumber = cardService.generateCardNumber(productId, holderName);

        assertNotNull(cardNumber);
        assertTrue(cardNumber.startsWith(expectedCardPrefix));
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void testActivateCard() {
        String cardId = "1234560000000001";
        Card card = new Card();
        card.setCardId(cardId);
        card.setActive(false);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        Card activatedCard = cardService.activateCard(cardId);

        assertTrue(activatedCard.isActive());
        verify(cardRepository, times(1)).findById(cardId);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testBlockCard() {
        String cardId = "1234560000000001";
        Card card = new Card();
        card.setCardId(cardId);
        card.setBlocked(false);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        Card blockedCard = cardService.blockCard(cardId);

        assertTrue(blockedCard.isBlocked());
        verify(cardRepository, times(1)).findById(cardId);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testReloadBalance() {
        String cardId = "1234560000000001";
        BigDecimal initialBalance = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.valueOf(100);
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(initialBalance);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        Card reloadedCard = cardService.reloadBalance(cardId, amount);

        assertEquals(initialBalance.add(amount), reloadedCard.getBalance());
        verify(cardRepository, times(1)).findById(cardId);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testGetBalance() {
        String cardId = "1234560000000001";
        BigDecimal balance = BigDecimal.valueOf(100);
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(balance);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        BigDecimal retrievedBalance = cardService.getBalance(cardId);

        assertEquals(balance, retrievedBalance);
        verify(cardRepository, times(1)).findById(cardId);
    }
}
