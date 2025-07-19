package org.hiring.api.common.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hiring.api.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALIDATION_ERROR_CODE = "INVALID_INPUT";
    private static final String NOT_FOUND_ERROR_CODE = "RESOURCE_NOT_FOUND";
    private static final String INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR";

    /**
     * DTO의 @Valid 어노테이션 유효성 검사 실패 시 발생하는 MethodArgumentNotValidException을 처리합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final var bindingResult = e.getBindingResult();

        // 여러 개의 유효성 검사 에러 메시지를 ", "로 연결하여 하나의 문자열로 만듭니다.
        final var errorMessage = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("MethodArgumentNotValidException: {}", errorMessage, e);

        return ResponseEntity.badRequest()
                .body(
                        ErrorResponse.of(VALIDATION_ERROR_CODE, errorMessage)
                );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFoundException(final EntityNotFoundException e) {
        log.warn("EntityNotFoundException: {}", e.getMessage(), e);
        return ResponseEntity.notFound()
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(final Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(
                        ErrorResponse.of(INTERNAL_SERVER_ERROR_CODE, e.getMessage())
                );
    }
}
