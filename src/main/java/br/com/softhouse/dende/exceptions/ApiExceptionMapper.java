package br.com.softhouse.dende.exceptions;

import br.com.dende.softhouse.process.route.ResponseEntity;
import br.com.softhouse.dende.dto.ApiResponse;

/**
 * Centraliza a tradução das exceções da aplicação em respostas HTTP padronizadas.
 */
public final class ApiExceptionMapper {

    private ApiExceptionMapper() {
    }

    public static <T> ResponseEntity<ApiResponse<T>> toResponse(Throwable throwable) {
        int statusCode = statusCodeFor(throwable);
        String titulo = titleFor(statusCode);
        return ResponseEntity.status(statusCode, new ApiResponse<>(messageFor(throwable), statusCode, titulo));
    }

    private static int statusCodeFor(Throwable throwable) {
        if (throwable instanceof UnauthorizedException) {
            return 401;
        }
        if (throwable instanceof NotFoundException) {
            return 404;
        }
        if (throwable instanceof ConflictException) {
            return 409;
        }
        if (throwable instanceof ValidationException) {
            return 400;
        }
        return 500;
    }

    private static String titleFor(int statusCode) {
        switch (statusCode) {
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 404:
                return "Not Found";
            case 409:
                return "Conflict";
            default:
                return "Internal Server Error";
        }
    }

    private static String messageFor(Throwable throwable) {
        if (throwable == null || throwable.getMessage() == null || throwable.getMessage().trim().isEmpty()) {
            return "Erro inesperado";
        }
        return throwable.getMessage();
    }
}

