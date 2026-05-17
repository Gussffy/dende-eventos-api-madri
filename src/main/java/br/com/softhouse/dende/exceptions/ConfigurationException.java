package br.com.softhouse.dende.exceptions;

public class ConfigurationException extends UncheckedException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

