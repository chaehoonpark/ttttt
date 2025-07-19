package org.hiring.api.repository.company.query;

import java.util.List;
import org.hiring.api.domain.Company;
import org.hiring.api.entity.CompanyJpaEntity;
import org.springframework.stereotype.Repository;

public interface CompanyQueryRepository {

    List<CompanyJpaEntity> loadCompanies(CompanySearchCondition condition);
    long countCompanies(CompanySearchCondition condition);

}
