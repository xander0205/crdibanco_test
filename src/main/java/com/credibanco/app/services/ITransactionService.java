package com.credibanco.app.services;

import java.math.BigDecimal;

import com.credibanco.app.dtos.TransactionDTO;

public interface ITransactionService {

	TransactionDTO purchase(String cardId, BigDecimal amount);

	TransactionDTO cancelTransaction(String cardId, Long transactionId);

	TransactionDTO getTransaction(Long transactionId);

}
