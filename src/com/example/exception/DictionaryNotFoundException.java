package com.example.exception;

public class DictionaryNotFoundException extends RuntimeException {

    public DictionaryNotFoundException(String message) {
        super(message);
    }
}
