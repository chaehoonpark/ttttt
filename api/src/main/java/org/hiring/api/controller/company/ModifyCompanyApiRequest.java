package org.hiring.api.controller.company;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hiring.api.service.company.ModifyCompanyServiceRequest;

import java.beans.ConstructorProperties;

public record ModifyCompanyApiRequest(
        // 수정 요청에서는 값이 들어온 필드만 검증합니다. (null 허용)
        @Size(max = 100, message = "회사명은 100자를 넘을 수 없습니다.")
        String name,

        @Size(max = 100, message = "산업군은 100자를 넘을 수 없습니다.")
        String industry,

        String description,

        @Size(max = 50, message = "직원 수는 50자를 넘을 수 없습니다.")
        String employeeCount,

        @Positive(message = "설립연도는 양수여야 합니다.")
        @Max(value = 2500, message = "설립연도는 2500년을 넘을 수 없습니다.")
        Integer foundedYear,

        @Size(max = 200, message = "로고 URL은 200자를 넘을 수 없습니다.")
        String logoUrl,

        @Size(max = 200, message = "웹사이트 URL은 200자를 넘을 수 없습니다.")
        String websiteUrl,

        @Size(max = 200, message = "주소는 200자를 넘을 수 없습니다.")
        String address
) {

    @ConstructorProperties({"name", "industry", "description", "employeeCount", "foundedYear", "logoUrl", "websiteUrl", "address"})
    public ModifyCompanyApiRequest{}

    public ModifyCompanyServiceRequest toServiceRequest(Long companyId) {
        return new ModifyCompanyServiceRequest(
                companyId, name, industry, description, employeeCount,
                foundedYear, logoUrl, websiteUrl, address
        );
    }
}