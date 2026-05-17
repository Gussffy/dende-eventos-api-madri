package br.com.softhouse.dende.exceptions;

public class ConflictException extends UncheckedException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}

