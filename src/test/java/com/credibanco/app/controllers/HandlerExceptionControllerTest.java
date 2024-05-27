package com.credibanco.app.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.credibanco.app.controller.HandlerExceptionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.credibanco.app.dtos.Error;
import com.credibanco.app.exceptions.MessageNotFoundException;

class HandlerExceptionControllerTest {

    @Mock
    private Environment messages;

    @InjectMocks
    private HandlerExceptionController handlerExceptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNotFoundException() {
        String exceptionMessage = "Card not found";
        String propertyMessage = "Resource not found";

        when(messages.getProperty("message.response.exceptionsApiNotFound")).thenReturn(propertyMessage);

        MessageNotFoundException exception = new MessageNotFoundException(exceptionMessage);

        ResponseEntity<Error> response = handlerExceptionController.notFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(propertyMessage, response.getBody().getMessage());
        assertEquals(exceptionMessage, response.getBody().getErrorMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertNotNull(response.getBody().getDate());

        verify(messages, times(1)).getProperty("message.response.exceptionsApiNotFound");
    }
}

