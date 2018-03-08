package com.tourneynizer.tourneynizer.error;

public class InternalErrorException extends Exception {
    public InternalErrorException(Exception e) {
        super(e);
    }

    public InternalErrorException(String str) {
        super(str);
    }
}
