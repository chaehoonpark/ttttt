package org.hiring.api.controller.company;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.BaseResponse;
import org.example.PagedResult;
import org.hiring.api.domain.Company;
import org.hiring.api.service.company.usecase.LoadCompanyUseCase;
import org.hiring.api.service.company.usecase.ModifyCompanyUseCase;
import org.hiring.api.service.company.usecase.RegisterCompanyUseCase;
import org.hiring.api.service.company.usecase.RemoveCompanyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final RegisterCompanyUseCase registerCompanyUseCase;
    private final LoadCompanyUseCase loadCompanyUseCase;
    private final ModifyCompanyUseCase modifyCompanyUseCase;
    private final RemoveCompanyUseCase removeCompanyUseCase;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> registerCompany(
        @Valid @RequestBody RegisterCompanyApiRequest request
    ) {
        registerCompanyUseCase.registerCompany(request.toServiceRequest());
        return ResponseEntity.ok(BaseResponse.success());
    }

    @PatchMapping("/{companyId}")
    public ResponseEntity<BaseResponse<Void>> modifyCompany(
        @PathVariable Long companyId,
        @Valid @RequestBody ModifyCompanyApiRequest request
    ) {
        modifyCompanyUseCase.modifyCompany(request.toServiceRequest(companyId));
        return ResponseEntity.ok(BaseResponse.success());
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<BaseResponse<Void>> removeCompany(
        @PathVariable Long companyId
    ) {
        removeCompanyUseCase.removeCompany(companyId);
        return ResponseEntity.ok(BaseResponse.success());
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<BaseResponse<Company>> loadCompany(
        @PathVariable Long companyId
    ) {
        Company company = loadCompanyUseCase.loadCompany(companyId);
        return ResponseEntity.ok(BaseResponse.success(company));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PagedResult<Company>>> loadCompanies(
        @Valid LoadCompaniesApiRequest request
    ) {
        PagedResult<Company> companies = loadCompanyUseCase.loadCompanies(request.toServiceRequest());
        return ResponseEntity.ok(BaseResponse.success(companies));
    }
}