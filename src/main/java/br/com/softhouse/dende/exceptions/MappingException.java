package br.com.softhouse.dende.exceptions;

public class MappingException extends UncheckedException {

    public MappingException(String message) {
        super(message);
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}

