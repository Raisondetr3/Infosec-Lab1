package ru.itmo.infosec.exception;

public class PostNotFoundException  extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
