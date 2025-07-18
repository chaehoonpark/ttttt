package org.hiring.api.repository.company.query;

import static org.hiring.api.entity.QCompanyJpaEntity.companyJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hiring.api.entity.CompanyJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyQueryRepositoryImpl implements CompanyQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CompanyJpaEntity> loadCompanies(CompanySearchCondition condition) {
        return queryFactory
            .selectFrom(companyJpaEntity)
            .where(
                addressContains(condition.getAddress()),
                industryContains(condition.getIndustry()),
                keywordsContains(condition.getKeywords())
            )
            .orderBy(companyJpaEntity.createdAt.desc())
            .offset(condition.getOffset())
            .limit(condition.getLimit())
            .fetch();
    }


    @Override
    public long countCompanies(CompanySearchCondition condition) {
        Long count = queryFactory
            .select(companyJpaEntity.count())
            .from(companyJpaEntity)
            .where(
                addressContains(condition.getAddress()),
                industryContains(condition.getIndustry()),
                keywordsContains(condition.getKeywords())
            )
            .fetchOne();

        return Objects.isNull(count) ? 0L : count;
    }

    private BooleanExpression addressContains(String address) {
        if (address == null || address.trim().isEmpty()) {
            return null;
        }
        return companyJpaEntity.address.containsIgnoreCase(address);
    }

    private BooleanExpression industryContains(String industry) {
        if (industry == null || industry.trim().isEmpty()) {
            return null;
        }
        return companyJpaEntity.industry.containsIgnoreCase(industry);
    }

    private BooleanExpression keywordsContains(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return null;
        }
        BooleanExpression expression = null;
        for (String keyword : keywords) {
            if (expression == null) {
                expression = companyJpaEntity.name.containsIgnoreCase(keyword)
                    .or(companyJpaEntity.description.containsIgnoreCase(keyword));
            } else {
                expression = expression.or(companyJpaEntity.name.containsIgnoreCase(keyword)
                    .or(companyJpaEntity.description.containsIgnoreCase(keyword)));
            }
        }
        return expression;
    }
}
