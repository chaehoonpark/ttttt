package org.hiring.api.service.company;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.service.company.usecase.ModifyCompanyUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ModifyCompanyService implements ModifyCompanyUseCase {

    private final CompanyRepository companyRepository;

    @Override
    public void modifyCompany(ModifyCompanyServiceRequest request) {
        CompanyJpaEntity companyEntity = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + request.companyId()));

        companyEntity.modifyCompany(
            request.name(),
            request.industry(),
            request.description(),
            request.employeeCount(),
            request.foundedYear(),
            request.logoUrl(),
            request.websiteUrl(),
            request.address()
        );
    }
}
