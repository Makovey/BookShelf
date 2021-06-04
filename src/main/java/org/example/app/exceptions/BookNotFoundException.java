package org.example.app.exceptions;


public class BookNotFoundException extends Exception {
    private final String message;

    public BookNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
