package edu.miu.webstorebackend.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super("Type not found: " + s);
    }
}
