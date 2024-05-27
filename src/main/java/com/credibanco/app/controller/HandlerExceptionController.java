package com.credibanco.app.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.credibanco.app.dtos.Error;
import com.credibanco.app.exceptions.MessageNotFoundException;

@RestControllerAdvice
public class HandlerExceptionController {
	
	@Autowired
	private Environment messages;
	
	@ExceptionHandler(MessageNotFoundException.class)
	public ResponseEntity<Error> notFoundException(MessageNotFoundException ex){
		Error error = new Error();
		error.setMessage(messages.getProperty("message.response.exceptionsApiNotFound"));
		error.setErrorMessage(ex.getMessage());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setDate(new Date());
		return ResponseEntity.status(error.getStatus()).body(error);
		
	}
	
}
