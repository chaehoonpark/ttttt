package org.example;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    // 성공 응답 (데이터 포함)
    public static <T> BaseResponse<T> success(final T data) {
        return new BaseResponse<>(true, data, null);
    }

    // 성공 응답 (데이터 없음)
    public static BaseResponse<Void> success() {
        return new BaseResponse<>(true, null, null);
    }

    // 실패 응답
    public static <T> BaseResponse<T> error(String code, String message) {
        return new BaseResponse<>(false, null, new ErrorResponse(code, message));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
