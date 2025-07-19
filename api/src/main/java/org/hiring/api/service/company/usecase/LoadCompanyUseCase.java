package org.hiring.api.service.company.usecase;


import org.hiring.api.common.response.PagedResult;
import org.hiring.api.domain.Company;
import org.hiring.api.service.company.LoadCompaniesServiceRequest;

public interface LoadCompanyUseCase {
    PagedResult<Company> loadCompanies(LoadCompaniesServiceRequest request);
    Company loadCompany(Long companyId);
}
