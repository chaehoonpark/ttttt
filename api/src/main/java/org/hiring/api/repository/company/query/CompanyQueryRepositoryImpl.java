package org.hiring.api.repository.company.query;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.hiring.api.entity.CompanyJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static org.hiring.api.entity.QCompanyJpaEntity.companyJpaEntity;

@Repository
@RequiredArgsConstructor
public class CompanyQueryRepositoryImpl implements CompanyQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CompanyJpaEntity> loadCompaniesPage(final CompanySearchCondition condition) {
        final var query = queryFactory
                .selectFrom(companyJpaEntity)
                .where(
                        nameContains(condition.getName()),
                        descriptionContains(condition.getDescription()),
                        addressContains(condition.getAddress()),
                        industryContains(condition.getIndustry())
                )
                .orderBy(companyJpaEntity.createdAt.desc())
                .offset(condition.getOffset())
                .limit(condition.getLimit());

        return PageableExecutionUtils.getPage(query.fetch(),
                Pageable.ofSize(condition.getLimit()),
                query::fetchCount);
    }


    @Override
    public long countCompanies(CompanySearchCondition condition) {
        final var count = queryFactory
                .select(companyJpaEntity.count())
                .from(companyJpaEntity)
                .where(
                        nameContains(condition.getName()),
                        descriptionContains(condition.getDescription()),
                        addressContains(condition.getAddress()),
                        industryContains(condition.getIndustry())
                )
                .fetchOne();

        return Objects.isNull(count) ? 0L : count;
    }

    private BooleanExpression addressContains(final String address) {
        return StringUtils.hasText(address) ? companyJpaEntity.address.containsIgnoreCase(address) : null;
    }

    private BooleanExpression industryContains(String industry) {
        return StringUtils.hasText(industry) ? companyJpaEntity.industry.containsIgnoreCase(industry) : null;
    }

    private BooleanExpression descriptionContains(String description) {
        return StringUtils.hasText(description) ? companyJpaEntity.description.containsIgnoreCase(description) : null;
    }

    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? companyJpaEntity.name.containsIgnoreCase(name) : null;
    }
}
