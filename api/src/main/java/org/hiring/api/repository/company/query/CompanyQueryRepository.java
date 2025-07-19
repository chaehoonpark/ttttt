package org.hiring.api.repository.company.query;

import org.hiring.api.entity.CompanyJpaEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyQueryRepository {

    Page<CompanyJpaEntity> loadCompaniesPage(final CompanySearchCondition condition);
    List<CompanyJpaEntity> loadCompanies(final CompanySearchCondition condition);
    long countCompanies(CompanySearchCondition condition);

}
