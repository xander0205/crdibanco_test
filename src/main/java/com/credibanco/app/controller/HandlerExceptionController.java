package com.credibanco.app.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.credibanco.app.dtos.Error;
import com.credibanco.app.exceptions.MessageNotFoundException;

@RestControllerAdvice
public class HandlerExceptionController {
	
	@Autowired
	private Environment messages;
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Error> notFoundException(NoHandlerFoundException ex){
		Error error = new Error();
		error.setMessage(messages.getProperty("message.response.exceptionsApiNotFound"));
		error.setErrorMessage(ex.getMessage());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setDate(new Date());
		return ResponseEntity.status(error.getStatus()).body(error);
		
	}
	
	@ExceptionHandler({NullPointerException.class, MessageNotFoundException.class})
	public ResponseEntity<Error> nullPointerException(Exception ex){
		Error error = new Error();
		error.setMessage(messages.getProperty("message.response.exceptionsNullPointer"));
		error.setErrorMessage(ex.getMessage());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setDate(new Date());
		return ResponseEntity.status(error.getStatus()).body(error);
		
	}

}
