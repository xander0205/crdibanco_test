package com.credibanco.app.services;

import java.math.BigDecimal;

import com.credibanco.app.entities.Card;

public interface ICardService {

	String generateCardNumber(String productId, String holderName);

	Card activateCard(String cardId);

	Card blockCard(String cardId);

	Card reloadBalance(String cardId, BigDecimal amount);

	BigDecimal getBalance(String cardId);
	
	

}
