package com.example.exception;

public class DictionaryEmptyExeption extends RuntimeException {

    public DictionaryEmptyExeption(String message) {
        super(message);
    }
}
