package com.dreamteam.tourneynizer.model;

public class ErrorMessage {

    private final String message;

    public ErrorMessage(Exception e) {
        this.message = e.getMessage();
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
