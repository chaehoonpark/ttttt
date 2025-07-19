package org.hiring.api.repository.job.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EmploymentType;
import org.springframework.stereotype.Repository;

import static org.hiring.api.entity.QCompanyJpaEntity.companyJpaEntity;
import static org.hiring.api.entity.QJobJpaEntity.jobJpaEntity;

@Repository
@RequiredArgsConstructor
public class JobQueryRepositoryImpl implements JobQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<JobJpaEntity> loadJobs(JobSearchCondition condition) {
        return queryFactory
            .selectFrom(jobJpaEntity)
            .leftJoin(jobJpaEntity.company, companyJpaEntity)
            .fetchJoin()
            .where(
                keywordContains(condition.getKeyword()),
                cityEquals(condition.getCity()),
                districtEquals(condition.getDistrict()),
                employmentEquals(condition.getEmploymentType())
            )
            .orderBy(jobJpaEntity.createdAt.desc())
            .offset(condition.getOffset())
            .limit(condition.getLimit())
            .fetch();
    }

    @Override
    public long countJobs(JobSearchCondition condition) {
        final var count = queryFactory
            .select(jobJpaEntity.count())
            .from(jobJpaEntity)
            .leftJoin(jobJpaEntity.company, companyJpaEntity)
            .where(
                keywordContains(condition.getKeyword()),
                cityEquals(condition.getCity()),
                districtEquals(condition.getDistrict()),
                employmentEquals(condition.getEmploymentType())
            )
            .fetchOne();

        return Objects.isNull(count) ? 0 : count;
    }

    private BooleanExpression keywordContains(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()) {
            return null;
        }
        return jobJpaEntity.title.containsIgnoreCase(keyword)
            .or(companyJpaEntity.name.containsIgnoreCase(keyword));
    }

    private BooleanExpression cityEquals(CityEnum city) {
        return city != null ? jobJpaEntity.city.eq(city) : null;
    }

    private BooleanExpression districtEquals(DistrictEnum district) {
        return district != null ? jobJpaEntity.district.eq(district) : null;
    }

    private BooleanExpression employmentEquals(EmploymentType employmentType) {
        return employmentType != null ? jobJpaEntity.employmentType.eq(employmentType) : null;
    }

}
