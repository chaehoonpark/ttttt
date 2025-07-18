package org.hiring.api.common.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        // 여러 개의 유효성 검사 에러 메시지를 ", "로 연결하여 하나의 문자열로 만듭니다.
        String errorMessage = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("MethodArgumentNotValidException: {}", errorMessage);

        // common 모듈에 있는 BaseResponse.error 정적 메서드를 사용하여 실패 응답을 생성합니다.
        BaseResponse<Void> response = BaseResponse.error(VALIDATION_ERROR_CODE, errorMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("EntityNotFoundException: {}", e.getMessage());
        BaseResponse<Void> response = BaseResponse.error(NOT_FOUND_ERROR_CODE, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleGlobalException(Exception e) {
        log.error("Unhandled Exception: ", e); // 예상치 못한 오류이므로 error 레벨로 로깅
        BaseResponse<Void> response = BaseResponse.error(INTERNAL_SERVER_ERROR_CODE, "서버 내부 오류가 발생했습니다.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}