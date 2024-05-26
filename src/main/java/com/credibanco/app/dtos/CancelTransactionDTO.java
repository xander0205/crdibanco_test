package com.credibanco.app.dtos;

public class CancelTransactionDTO {
    private String cardId;
    private Long transactionId;
    
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

    
}
