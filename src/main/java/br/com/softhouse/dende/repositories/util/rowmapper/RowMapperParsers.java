package br.com.softhouse.dende.repositories.util.rowmapper;

import br.com.softhouse.dende.exceptions.MappingException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

final class RowMapperParsers {

    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    };

    private RowMapperParsers() {
    }

    static String text(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    static Long toLong(String value) {
        String text = text(value);
        if (text == null) {
            return null;
        }
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            throw new MappingException("Valor numérico inválido para Long: " + value, e);
        }
    }

    static Integer toInteger(String value) {
        String text = text(value);
        if (text == null) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new MappingException("Valor numérico inválido para Integer: " + value, e);
        }
    }

    static Double toDouble(String value) {
        String text = text(value);
        if (text == null) {
            return null;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new MappingException("Valor numérico inválido para Double: " + value, e);
        }
    }

    static BigDecimal toBigDecimal(String value) {
        String text = text(value);
        if (text == null) {
            return null;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            throw new MappingException("Valor numérico inválido para BigDecimal: " + value, e);
        }
    }

    static Boolean toBoolean(String value) {
        String text = text(value);
        if (text == null) {
            return null;
        }

        String normalized = text.toLowerCase();
        if ("1".equals(normalized) || "true".equals(normalized)) {
            return true;
        }
        if ("0".equals(normalized) || "false".equals(normalized)) {
            return false;
        }
        throw new MappingException("Valor booleano inválido: " + value);
    }

    static LocalDate toLocalDate(String value) {
        String text = text(value);
        if (text == null) {
            return null;
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new MappingException("Data inválida: " + value, e);
        }
    }

    static LocalDateTime toLocalDateTime(String value) {
        String text = text(value);
        if (text == null) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(text, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next format.
            }
        }

        try {
            return LocalDateTime.parse(text);
        } catch (DateTimeParseException e) {
            throw new MappingException("Data/hora inválida: " + value, e);
        }
    }
}

