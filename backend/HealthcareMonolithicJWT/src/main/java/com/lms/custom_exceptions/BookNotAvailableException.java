package com.lms.custom_exceptions;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String message) { super(message); }
}
