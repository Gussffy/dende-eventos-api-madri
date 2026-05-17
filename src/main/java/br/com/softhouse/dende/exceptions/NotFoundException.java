package br.com.softhouse.dende.exceptions;

public class NotFoundException extends UncheckedException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

