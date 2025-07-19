package org.hiring.api.common.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
