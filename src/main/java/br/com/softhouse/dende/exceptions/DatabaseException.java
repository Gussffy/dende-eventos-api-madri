package br.com.softhouse.dende.exceptions;

public class DatabaseException extends UncheckedException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

