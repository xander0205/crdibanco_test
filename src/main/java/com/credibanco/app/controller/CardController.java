package com.credibanco.app.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.app.entities.Card;
import com.credibanco.app.services.CardService;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    private CardService cardService;

    @GetMapping("/{productId}/number")
    public String generateCardNumber(@PathVariable String productId) {
        return cardService.generateCardNumber(productId);
    }

    @PostMapping("/enroll")
    public ResponseEntity<Card> activateCard(@RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(cardService.activateCard(payload.get("cardId")));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Card> blockCard(@PathVariable String cardId) {
        return ResponseEntity.ok(cardService.blockCard(cardId));
    }

    @PostMapping("/balance")
    public ResponseEntity<Card> reloadBalance(@RequestBody Map<String, Object> payload) {
        String cardId = (String) payload.get("cardId");
        BigDecimal amount = new BigDecimal((String) payload.get("balance"));
        return ResponseEntity.ok(cardService.reloadBalance(cardId, amount));
    }

    @GetMapping("/balance/{cardId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String cardId) {
        return ResponseEntity.ok(cardService.getBalance(cardId));
    }
}
