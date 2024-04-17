package com.example.financetracker.exceptions;

public record ErrorResponse(String message) {
    public String getMessage() {
        return message;
    }
}