package com.credibanco.app.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credibanco.app.entities.Card;
import com.credibanco.app.repositories.CardRepository;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public String generateCardNumber(String productId, String holderName) {
        String cardNumber = productId + String.format("%010d", new Random().nextInt(1000000000));
        Card card = new Card();
        card.setCardId(cardNumber);
        card.setProductName(productId.equals("123456") ? "Tarjeta de Crédito" : "Tarjeta de Débito");
        card.setHolderName(holderName);
        card.setExpirationDate(LocalDate.now().plusYears(3));
        card.setActive(false);
        card.setBlocked(false);
        card.setBalance(BigDecimal.ZERO);
        cardRepository.save(card);
        return cardNumber;
    }

    public Card activateCard(String cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        card.setActive(true);
        return cardRepository.save(card);
    }

    public Card blockCard(String cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        card.setBlocked(true);
        return cardRepository.save(card);
    }

    public Card reloadBalance(String cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        card.setBalance(card.getBalance().add(amount));
        return cardRepository.save(card);
    }

    public BigDecimal getBalance(String cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        return card.getBalance();
    }
   
}
