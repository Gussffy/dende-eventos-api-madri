package br.com.softhouse.dende.repositories.util.rowmapper;

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
        return text == null ? null : Long.parseLong(text);
    }

    static Integer toInteger(String value) {
        String text = text(value);
        return text == null ? null : Integer.parseInt(text);
    }

    static Double toDouble(String value) {
        String text = text(value);
        return text == null ? null : Double.parseDouble(text);
    }

    static BigDecimal toBigDecimal(String value) {
        String text = text(value);
        return text == null ? null : new BigDecimal(text);
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
        throw new IllegalArgumentException("Valor booleano inválido: " + value);
    }

    static LocalDate toLocalDate(String value) {
        String text = text(value);
        return text == null ? null : LocalDate.parse(text);
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

        return LocalDateTime.parse(text);
    }
}

