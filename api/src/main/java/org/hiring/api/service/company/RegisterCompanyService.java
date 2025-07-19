package org.hiring.api.service.company;

import lombok.RequiredArgsConstructor;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.service.company.usecase.RegisterCompanyUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterCompanyService implements RegisterCompanyUseCase {

    private final CompanyRepository companyRepository;

    @Override
    public void registerCompany(final RegisterCompanyServiceRequest request) {
        final var companyEntity = CompanyJpaEntity.builder()
            .name(request.name())
            .industry(request.industry())
            .description(request.description())
            .employeeCount(request.employeeCount())
            .foundedYear(request.foundedYear())
            .logoUrl(request.logoUrl())
            .websiteUrl(request.websiteUrl())
            .address(request.address())
            .build();

        companyRepository.save(companyEntity);
    }
}
