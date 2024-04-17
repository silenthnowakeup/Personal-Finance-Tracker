package com.example.financetracker.exceptions;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExceptionsTests {
    ExceptionsHandler exceptionsHandler = new ExceptionsHandler();

    @Test
    void testHandleInternalServerError() {
        // Mocking the RuntimeException
        RuntimeException ex = mock(RuntimeException.class);
        when(ex.getMessage()).thenReturn("Internal Server Error");

        // Testing the method
        ErrorResponse response = exceptionsHandler.handleInternalServerError(ex);

        // Assertions
        assertEquals("Internal Server Error", response.getMessage());
    }

    @Test
    void testHandleHttpClientErrorException() {
        // Setup
        Exception ex = new HttpClientErrorException(HttpStatus.BAD_REQUEST);

        // Execute
        ErrorResponse response = exceptionsHandler.handleBadRequestException(ex);

        // Verify
        assertEquals("400 ERROR, BAD REQUEST", response.getMessage());
    }

    @Test
    void testHandleMethodNotAllowed() {
        // Setup
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("GET");

        // Execute
        ErrorResponse response = exceptionsHandler.handleMethodNotAllowed(ex);

        // Verify
        assertEquals("405 ERROR, METHOD NOT ALLOWED", response.getMessage());
    }

    @Mock
    NoHandlerFoundException ex;

    @Test
    void testHandlerFoundException() {
        ExceptionsHandler handler = new ExceptionsHandler();
        ErrorResponse response = handler.handlerFoundException(ex);
        assertEquals("404 ERROR, NOT FOUND", response.getMessage());
    }
}
