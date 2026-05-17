package br.com.softhouse.dende.exceptions;

public class UnauthorizedException extends UncheckedException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

