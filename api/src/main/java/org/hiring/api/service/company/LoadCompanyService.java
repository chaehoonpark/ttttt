package org.hiring.api.service.company;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.PagedResult;
import org.hiring.api.domain.Company;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.mapper.CompanyMapper;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.repository.company.query.CompanyQueryRepository;
import org.hiring.api.repository.company.query.CompanySearchCondition;
import org.hiring.api.service.company.usecase.LoadCompanyUseCase;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadCompanyService implements LoadCompanyUseCase {

    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;
    private final CompanyQueryRepository companyQueryRepository;

    @Override
    @Cacheable(value = "companies", key = "#request")
    public PagedResult<Company> loadCompanies(final LoadCompaniesServiceRequest request) {
        CompanySearchCondition condition = CompanySearchCondition.builder()
            .address(request.location())
            .industry(request.industry())
            .keywords(request.keywords())
            .limit(request.getLimit())
            .offset(request.getOffset())
            .build();

        List<CompanyJpaEntity> companyEntities = companyQueryRepository.loadCompanies(condition);

        long totalCount = companyQueryRepository.countCompanies(condition);

        List<Company> companies = companyEntities.stream()
            .map(companyMapper::toModel)
            .toList();

        return PagedResult.of(
            companies,
            request.page(),
            request.size(),
            totalCount
        );
    }

    @Override
    public Company loadCompany(final Long companyId) {
        final var companyEntity = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        return companyMapper.toModel(companyEntity);
    }
}
