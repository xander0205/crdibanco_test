package com.credibanco.app.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.credibanco.app.entities.Transaction;
import com.credibanco.app.exceptions.MessageNotFoundException;
import com.credibanco.app.dtos.TransactionDTO;
import com.credibanco.app.entities.Card;
import com.credibanco.app.repositories.CardRepository;
import com.credibanco.app.repositories.TransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionService implements ITransactionService{
	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	private Environment messages;

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	@Override
	public TransactionDTO purchase(String cardId, BigDecimal amount) {
		Card card = cardRepository.findById(cardId).orElseThrow(
				() -> new MessageNotFoundException(messages.getProperty("message.response.cardNotFound") + cardId));

		if (!card.isActive()) {
			logger.error(messages.getProperty("message.response.transaction.cardNotActive"));
			throw new MessageNotFoundException(messages.getProperty("message.response.transaction.cardNotActive"));
		}
		if (card.isBlocked()) {
			logger.error(messages.getProperty("message.response.transaction.cardIsblocked"));
			throw new MessageNotFoundException(messages.getProperty("message.response.transaction.cardIsblocked"));
		}
		if (card.getBalance().compareTo(amount) < 0) {
			logger.error("Insufficient balance. Card balance: {}, Purchase amount: {}", card.getBalance(), amount);
			throw new MessageNotFoundException(
					messages.getProperty("message.response.transaction.insufficientBalance"));
		}
		if (card.getExpirationDate().isBefore(LocalDate.now())) {
			logger.error("Card has expired. Expiration date: {}", card.getExpirationDate());
			throw new MessageNotFoundException(messages.getProperty("message.response.transaction.cardExpired"));
		}

		card.setBalance(card.getBalance().subtract(amount));
		cardRepository.save(card);

		Transaction transaction = new Transaction();
		transaction.setCard(card);
		transaction.setAmount(amount);
		transaction.setTimestamp(LocalDateTime.now());
		transaction.setCancelled(false);
		transaction = transactionRepository.save(transaction);

		if (transaction == null) {
			throw new MessageNotFoundException(messages.getProperty("message.response.transaction.errSaveTrans"));
		}
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setTransactionId(transaction.getTransactionId());
		transactionDTO.setAmount(transaction.getAmount());
		transactionDTO.setTimestamp(transaction.getTimestamp());

		return transactionDTO;
	}
	
	@Override
	public TransactionDTO cancelTransaction(String cardId, Long transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
				() -> new MessageNotFoundException(messages.getProperty("message.response.transaction.transNotFound")));
		if (transaction.isCancelled()
				|| Duration.between(transaction.getTimestamp(), LocalDateTime.now()).toHours() > 24) {
			throw new MessageNotFoundException(messages.getProperty("message.response.transaction.transInvalidCancellation"));
		}
		Card card = cardRepository.findById(cardId).orElseThrow(
				() -> new MessageNotFoundException(messages.getProperty("message.response.cardNotFound") + cardId));
		card.setBalance(card.getBalance().add(transaction.getAmount()));
		transaction.setCancelled(true);
		transactionRepository.save(transaction);
		cardRepository.save(card);

		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setTransactionId(transaction.getTransactionId());
		transactionDTO.setAmount(transaction.getAmount());
		transactionDTO.setTimestamp(transaction.getTimestamp());
		transactionDTO.setCancelled(transaction.isCancelled());

		return transactionDTO;
	}
	
	@Override
	public TransactionDTO getTransaction(Long transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
				() -> new MessageNotFoundException(messages.getProperty("message.response.transaction.transNotFound")));

		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setTransactionId(transaction.getTransactionId());
		transactionDTO.setAmount(transaction.getAmount());
		transactionDTO.setTimestamp(transaction.getTimestamp());
		transactionDTO.setCancelled(transaction.isCancelled());

		return transactionDTO;
	}
}