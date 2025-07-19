package org.hiring.api.controller.company;

import jakarta.validation.constraints.*;
import org.hiring.api.service.company.RegisterCompanyServiceRequest;


public record RegisterCompanyApiRequest(
        @NotBlank(message = "회사명은 필수입니다.")
        @Size(max = 100, message = "회사명은 100자를 넘을 수 없습니다.")
        String name,

        @NotBlank(message = "산업군은 필수입니다.")
        @Size(max = 100, message = "산업군은 100자를 넘을 수 없습니다.")
        String industry,

        String description, // 설명은 선택 사항

        @Size(max = 50, message = "직원 수는 50자를 넘을 수 없습니다.")
        String employeeCount,

        @NotNull(message = "설립연도는 필수입니다.")
        @Positive(message = "설립연도는 양수여야 합니다.")
        @Max(value = 2500, message = "설립연도는 2500년을 넘을 수 없습니다.") // Entity 제약조건 반영
        Integer foundedYear,

        @Size(max = 200, message = "로고 URL은 200자를 넘을 수 없습니다.")
        String logoUrl,

        @Size(max = 200, message = "웹사이트 URL은 200자를 넘을 수 없습니다.")
        String websiteUrl,

        @NotBlank(message = "주소는 필수입니다.")
        @Size(max = 200, message = "주소는 200자를 넘을 수 없습니다.")
        String address
) {

    public RegisterCompanyServiceRequest toServiceRequest() {
        return new RegisterCompanyServiceRequest(
                name, industry, description, employeeCount,
                foundedYear, logoUrl, websiteUrl, address
        );
    }
}
