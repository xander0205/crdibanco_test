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

import com.credibanco.app.controller.CardController;
import com.credibanco.app.dtos.ActivateCardDTO;
import com.credibanco.app.dtos.GenerateCardNumberDTO;
import com.credibanco.app.dtos.ReloadBalanceDTO;
import com.credibanco.app.entities.Card;
import com.credibanco.app.services.ICardService;

class CardControllerTest {

    @Mock
    private ICardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCardNumber() {
        GenerateCardNumberDTO dto = new GenerateCardNumberDTO();
        dto.setProductId("123456");
        dto.setHolderName("CrediBanco");
        
        when(cardService.generateCardNumber(dto.getProductId(), dto.getHolderName())).thenReturn("1234567890123456");

        String cardNumber = cardController.generateCardNumber(dto);

        assertNotNull(cardNumber);
        assertEquals("1234567890123456", cardNumber);
        verify(cardService, times(1)).generateCardNumber(dto.getProductId(), dto.getHolderName());
    }

    @Test
    void testActivateCard() {
        ActivateCardDTO dto = new ActivateCardDTO();
        dto.setCardId("1234567890123456");
        
        Card card = new Card();
        card.setCardId("1234567890123456");
        card.setActive(true);
        
        when(cardService.activateCard(dto.getCardId())).thenReturn(card);

        ResponseEntity<Card> response = cardController.activateCard(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
        verify(cardService, times(1)).activateCard(dto.getCardId());
    }

    @Test
    void testBlockCard() {
        String cardId = "1234567890123456";
        
        Card card = new Card();
        card.setCardId(cardId);
        card.setBlocked(true);
        
        when(cardService.blockCard(cardId)).thenReturn(card);

        ResponseEntity<Card> response = cardController.blockCard(cardId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
        verify(cardService, times(1)).blockCard(cardId);
    }

    @Test
    void testReloadBalance() {
        ReloadBalanceDTO dto = new ReloadBalanceDTO();
        dto.setCardId("1234567890123456");
        dto.setBalance(BigDecimal.valueOf(100));
        
        Card card = new Card();
        card.setCardId("1234567890123456");
        card.setBalance(BigDecimal.valueOf(100));
        
        when(cardService.reloadBalance(dto.getCardId(), dto.getBalance())).thenReturn(card);

        ResponseEntity<Card> response = cardController.reloadBalance(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
        verify(cardService, times(1)).reloadBalance(dto.getCardId(), dto.getBalance());
    }

    @Test
    void testGetBalance() {
        String cardId = "1234567890123456";
        BigDecimal balance = BigDecimal.valueOf(100);
        
        when(cardService.getBalance(cardId)).thenReturn(balance);

        ResponseEntity<BigDecimal> response = cardController.getBalance(cardId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(balance, response.getBody());
        verify(cardService, times(1)).getBalance(cardId);
    }
}
