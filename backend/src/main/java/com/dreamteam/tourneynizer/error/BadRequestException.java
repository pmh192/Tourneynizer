package com.dreamteam.tourneynizer.error;

public class BadRequestException extends Exception {
    public BadRequestException(String message, Exception e) {
        super(message, e);
    }
}
