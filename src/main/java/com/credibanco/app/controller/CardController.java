package com.credibanco.app.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.app.dtos.ActivateCardDTO;
import com.credibanco.app.dtos.GenerateCardNumberDTO;
import com.credibanco.app.dtos.ReloadBalanceDTO;
import com.credibanco.app.entities.Card;
import com.credibanco.app.services.CardService;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping("/number")
    public String generateCardNumber(@RequestBody GenerateCardNumberDTO dto) {
        return cardService.generateCardNumber(dto.getProductId(), dto.getHolderName());
    }

    @PostMapping("/enroll")
    public ResponseEntity<Card> activateCard(@RequestBody ActivateCardDTO dto) {
        return ResponseEntity.ok(cardService.activateCard(dto.getCardId()));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Card> blockCard(@PathVariable String cardId) {
        return ResponseEntity.ok(cardService.blockCard(cardId));
    }

    @PostMapping("/balance")
    public ResponseEntity<Card> reloadBalance(@RequestBody ReloadBalanceDTO dto) {
        return ResponseEntity.ok(cardService.reloadBalance(dto.getCardId(), dto.getBalance()));
    }

    @GetMapping("/balance/{cardId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String cardId) {
        return ResponseEntity.ok(cardService.getBalance(cardId));
    }
}