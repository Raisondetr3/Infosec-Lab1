package ru.itmo.infosec.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {super(message);}
}
