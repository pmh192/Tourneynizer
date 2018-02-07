package com.tourneynizer.tourneynizer.error;

public class EmailTakenException extends Exception {
    public EmailTakenException(String message, Exception e) {
        super(message, e);
    }
}
