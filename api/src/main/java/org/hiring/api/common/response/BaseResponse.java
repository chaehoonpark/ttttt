package org.hiring.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponse<T>(
        boolean isSuccess,
        T data
) {
    // 성공 응답 (데이터 포함)
    public static <T> BaseResponse<T> success(final T data) {
        return new BaseResponse<>(true, data);
    }

    // 성공 응답 (데이터 없음)
    public static BaseResponse<Void> success() {
        return new BaseResponse<>(true, null);
    }

    public static BaseResponse<Void> created() {
        return new BaseResponse<>(true, null);
    }

    // 실패 응답
    public static BaseResponse<ErrorResponse> error(final String code, final String message) {
        return new BaseResponse<>(false, ErrorResponse.of(code, message));
    }
}
