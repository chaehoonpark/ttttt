package org.hiring.api.service.company;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.service.company.usecase.RemoveCompanyUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveCompanyService implements RemoveCompanyUseCase {

    private final CompanyRepository companyRepository;

    @Override
    public void removeCompany(Long companyId) {
        final var companyEntity = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        companyRepository.delete(companyEntity);
    }
}
