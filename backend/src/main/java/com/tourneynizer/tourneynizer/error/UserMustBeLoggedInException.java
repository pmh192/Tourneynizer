package com.tourneynizer.tourneynizer.error;

public class UserMustBeLoggedInException extends BadRequestException {
    public UserMustBeLoggedInException() {
        super("User is not logged in");
    }
}
