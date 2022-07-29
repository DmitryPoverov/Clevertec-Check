package ru.clevertec.exception;

public class WrongIdException extends RuntimeException {

    public WrongIdException(String message) {
        super(message);
    }
}
