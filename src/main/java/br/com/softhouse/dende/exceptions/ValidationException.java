package br.com.softhouse.dende.exceptions;

public class ValidationException extends UncheckedException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

