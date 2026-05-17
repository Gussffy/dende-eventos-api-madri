package br.com.softhouse.dende.exceptions;

/**
 * Exceção raiz não verificada da aplicação.
 */
public class UncheckedException extends RuntimeException {

    public UncheckedException(String message) {
        super(message);
    }

    public UncheckedException(String message, Throwable cause) {
        super(message, cause);
    }
}

