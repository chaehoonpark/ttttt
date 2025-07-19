package org.hiring.api.repository.job.query;

import org.hiring.api.common.AbstractJpaTest;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EducationLevel;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.entity.enums.ExperienceLevel;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.repository.job.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({JobQueryRepositoryImpl.class})
@DisplayName("JobQueryRepository 테스트")
class JobQueryRepositoryImplTest extends AbstractJpaTest {

    @Autowired
    private JobQueryRepository repository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("키워드 검색이 job title과 company name에서 동작한다")
    void searchKeywords() {
        // given
        // 1. Company 먼저 저장
        CompanyJpaEntity company1 = createCompany("테크솔루션", "IT");
        CompanyJpaEntity company2 = createCompany("일반회사", "금융");
        CompanyJpaEntity company3 = createCompany("다른회사", "제조");
        companyRepository.saveAll(List.of(company1, company2, company3));
        flushAndClear();

        // 2. Job 별도 저장 (연관관계의 주인)
        List<JobJpaEntity> jobs = List.of(createJob("백엔드 개발자", company1),           // title 매칭
                createJob("프론트엔드 개발자", company2),        // title 매칭
                createJob("일반 사무직", company3)              // 매칭 안됨
        );
        jobRepository.saveAll(jobs);
        flushAndClear();

        JobSearchCondition condition = JobSearchCondition
                .builder()
                .keyword("개발자")
                .city(null)
                .district(null)
                .employmentType(null)
                .offset(0)
                .limit(10)
                .build();

        // when
        List<JobJpaEntity> result = repository.loadJobs(condition);

        // then
        assertThat(result).hasSize(2).extracting("title").containsExactlyInAnyOrder("백엔드 개발자", "프론트엔드 개발자");
    }

    @Test
    @DisplayName("회사명으로 키워드 검색이 동작한다")
    void searchByCompanyName() {
        // given
        CompanyJpaEntity techCompany = createCompany("테크솔루션", "IT");
        CompanyJpaEntity normalCompany = createCompany("일반회사", "금융");
        companyRepository.saveAll(List.of(techCompany, normalCompany));
        flushAndClear();

        List<JobJpaEntity> jobs = List.of(createJob("백엔드 개발자", techCompany),         // 회사명 매칭
                createJob("프론트엔드 개발자", normalCompany)    // 매칭 안됨
        );
        jobRepository.saveAll(jobs);
        flushAndClear();

        JobSearchCondition condition = JobSearchCondition
                .builder()
                .keyword("테크")
                .city(null)
                .district(null)
                .employmentType(null)
                .offset(0)
                .limit(10)
                .build();

        // when
        List<JobJpaEntity> result = repository.loadJobs(condition);

        // then
        assertThat(result).hasSize(1).extracting("title").containsExactly("백엔드 개발자");
    }

    @Test
    @DisplayName("도시별 필터링이 정확히 동작한다")
    void filterByCity() {
        // given
        CompanyJpaEntity company = createCompany("테스트회사", "IT");
        companyRepository.save(company);
        flushAndClear();

        List<JobJpaEntity> jobs = List.of(createJobWithLocation("서울 개발자", company, CityEnum.SEOUL, DistrictEnum.SEOUL_GANGNAM), createJobWithLocation("서울 디자이너", company, CityEnum.SEOUL, DistrictEnum.SEOUL_GANGDONG), createJobWithLocation("부산 개발자", company, CityEnum.BUSAN, DistrictEnum.BUSAN_HAEUNDAE));
        jobRepository.saveAll(jobs);
        flushAndClear();

        JobSearchCondition condition = JobSearchCondition
                .builder()
                .keyword(null)
                .city(CityEnum.SEOUL)
                .district(null)
                .employmentType(null)
                .offset(0)
                .limit(10)
                .build();

        // when
        List<JobJpaEntity> result = repository.loadJobs(condition);

        // then
        assertThat(result).hasSize(2).extracting("city").containsOnly(CityEnum.SEOUL);
    }

    @Test
    @DisplayName("구별 필터링이 정확히 동작한다")
    void filterByDistrict() {
        // given
        CompanyJpaEntity company = createCompany("테스트회사", "IT");
        companyRepository.save(company);
        flushAndClear();

        List<JobJpaEntity> jobs = List.of(createJobWithLocation("강남 개발자", company, CityEnum.SEOUL, DistrictEnum.SEOUL_GANGNAM), createJobWithLocation("강동 개발자", company, CityEnum.SEOUL, DistrictEnum.SEOUL_GANGDONG), createJobWithLocation("해운대 개발자", company, CityEnum.BUSAN, DistrictEnum.BUSAN_HAEUNDAE));
        jobRepository.saveAll(jobs);
        flushAndClear();

        JobSearchCondition condition = JobSearchCondition
                .builder()
                .keyword(null)
                .city(null)
                .district(DistrictEnum.SEOUL_GANGNAM)
                .employmentType(null)
                .offset(0)
                .limit(10)
                .build();

        // when
        List<JobJpaEntity> result = repository.loadJobs(condition);

        // then
        assertThat(result).hasSize(1).extracting("district").containsOnly(DistrictEnum.SEOUL_GANGNAM);
    }

    @Test
    @DisplayName("고용형태별 필터링이 정확히 동작한다")
    void filterByEmploymentType() {
        // given
        CompanyJpaEntity company = createCompany("테스트회사", "IT");
        companyRepository.save(company);
        flushAndClear();

        List<JobJpaEntity> jobs = List.of(createJobWithEmploymentType("정규직 개발자", company, EmploymentType.FULL_TIME), createJobWithEmploymentType("계약직 개발자", company, EmploymentType.PART_TIME), createJobWithEmploymentType("인턴 개발자", company, EmploymentType.INTERN));
        jobRepository.saveAll(jobs);
        flushAndClear();

        JobSearchCondition condition = JobSearchCondition
                .builder()
                .keyword(null)
                .city(null)
                .district(null)
                .employmentType(EmploymentType.FULL_TIME)
                .offset(0)
                .limit(10)
                .build();

        // when
        List<JobJpaEntity> result = repository.loadJobs(condition);

        // then
        assertThat(result).hasSize(1).extracting("employmentType").containsOnly(EmploymentType.FULL_TIME);
    }

    @Test
    @DisplayName("복합 조건 검색시 AND 조건으로 동작한다")
    void complexSearchWithAndCondition() {
        // given
        CompanyJpaEntity company = createCompany("테스트회사", "IT");
        companyRepository.save(company);
        flushAndClear();

        List<JobJpaEntity> jobs = List.of(createComplexJob("서울 백엔드 개발자", company, CityEnum.SEOUL, DistrictEnum.SEOUL_GANGNAM, EmploymentType.FULL_TIME),    // 모든 조건 만족
                createComplexJob("서울 프론트엔드 개발자", company, CityEnum.SEOUL, DistrictEnum.SEOUL_GANGNAM, EmploymentType.PART_TIME), // 고용형태만 다름
                createComplexJob("부산 백엔드 개발자", company, CityEnum.BUSAN, DistrictEnum.BUSAN_HAEUNDAE, EmploymentType.FULL_TIME),  // 지역만 다름
                createComplexJob("서울 디자이너", company, CityEnum.SEOUL, DistrictEnum.SEOUL_GANGNAM, EmploymentType.FULL_TIME)        // 키워드만 다름
        );
        jobRepository.saveAll(jobs);
        flushAndClear();

        JobSearchCondition condition = JobSearchCondition
                .builder()
                .keyword("백엔드")
                .city(CityEnum.SEOUL)
                .district(DistrictEnum.SEOUL_GANGNAM)
                .employmentType(EmploymentType.FULL_TIME)
                .offset(0)
                .limit(10)
                .build();

        // when
        List<JobJpaEntity> result = repository.loadJobs(condition);

        // then
        assertThat(result).hasSize(1).extracting("title").containsExactly("서울 백엔드 개발자");
    }

    @Test
    @DisplayName("정렬이 createdAt 내림차순으로 동작한다")
    void orderByCreatedAtDesc() {
        // given
        CompanyJpaEntity company = createCompany("테스트회사", "IT");
        companyRepository.save(company);
        flushAndClear();

        JobJpaEntity oldJob = createJob("옛날 공고", company);
        jobRepository.save(oldJob);
        flushAndClear();

        JobJpaEntity newJob = createJob("새로운 공고", company);
        jobRepository.save(newJob);
        flushAndClear();

        JobSearchCondition condition = JobSearchCondition
                .builder()
                .keyword(null)
                .city(null)
                .district(null)
                .employmentType(null)
                .offset(0)
                .limit(10)
                .build();

        // when
        List<JobJpaEntity> result = repository.loadJobs(condition);

        // then
        assertThat(result).hasSize(2).extracting("title").containsExactly("새로운 공고", "옛날 공고"); // 최신순
    }

    @Test
    @DisplayName("count와 load 결과가 일치한다")
    void countAndLoadConsistency() {
        // given
        CompanyJpaEntity company = createCompany("테스트회사", "IT");
        companyRepository.save(company);
        em.flush();

        List<JobJpaEntity> jobs = List.of(createJobWithEmploymentType("정규직1", company, EmploymentType.FULL_TIME),
                createJobWithEmploymentType("정규직2", company, EmploymentType.FULL_TIME),
                createJobWithEmploymentType("계약직", company, EmploymentType.PART_TIME));
        jobRepository.saveAll(jobs);
        flushAndClear();

        JobSearchCondition condition = JobSearchCondition
                .builder()
                .keyword(null)
                .city(null)
                .district(null)
                .employmentType(EmploymentType.FULL_TIME)
                .offset(0)
                .limit(10)
                .build();

        // when
        List<JobJpaEntity> loadResult = repository.loadJobs(condition);
        long countResult = repository.countJobs(condition);

        // then
        assertThat(loadResult).hasSize((int) countResult);
        assertThat(countResult).isEqualTo(2);
    }

    // --- Helper methods --- //
    private CompanyJpaEntity createCompany(String name, String industry) {
        return fixtureMonkey
                .giveMeBuilder(CompanyJpaEntity.class)
                .set("id", null)
                .set("name", name)
                .set("industry", industry)
                .set("jobs", null)
                .sample();
    }

    private JobJpaEntity createJob(String title, CompanyJpaEntity company) {
        return fixtureMonkey
                .giveMeBuilder(JobJpaEntity.class)
                .set("id", null)
                .set("title", title)
                .set("company", company)
                .set("city", CityEnum.SEOUL)
                .set("district", DistrictEnum.SEOUL_GANGNAM)
                .set("employmentType", EmploymentType.FULL_TIME)
                .set("experienceLevel", ExperienceLevel.NEWCOMER)
                .set("educationLevel", EducationLevel.HIGH_SCHOOL)
                .set("postedAt", LocalDateTime.now())
                .set("deadline", LocalDateTime.now().plusDays(30))
                .sample();
    }

    private JobJpaEntity createJobWithLocation(String title, CompanyJpaEntity company, CityEnum city, DistrictEnum district) {
        return fixtureMonkey
                .giveMeBuilder(JobJpaEntity.class)
                .set("id", null)
                .set("title", title)
                .set("company", company)
                .set("city", city)
                .set("district", district)
                .set("employmentType", EmploymentType.FULL_TIME)
                .set("experienceLevel", ExperienceLevel.NEWCOMER)
                .set("educationLevel", EducationLevel.HIGH_SCHOOL)
                .set("postedAt", LocalDateTime.now())
                .set("deadline", LocalDateTime.now().plusDays(30))
                .sample();
    }

    private JobJpaEntity createJobWithEmploymentType(String title, CompanyJpaEntity company, EmploymentType employmentType) {
        return fixtureMonkey
                .giveMeBuilder(JobJpaEntity.class)
                .set("id", null)
                .set("title", title)
                .set("company", company)
                .set("city", CityEnum.SEOUL)
                .set("district", DistrictEnum.SEOUL_GANGNAM)
                .set("employmentType", employmentType)
                .set("experienceLevel", ExperienceLevel.NEWCOMER)
                .set("educationLevel", EducationLevel.HIGH_SCHOOL)
                .set("postedAt", LocalDateTime.now())
                .set("deadline", LocalDateTime.now().plusDays(30))
                .sample();
    }

    private JobJpaEntity createComplexJob(String title, CompanyJpaEntity company, CityEnum city, DistrictEnum district, EmploymentType employmentType) {
        return fixtureMonkey
                .giveMeBuilder(JobJpaEntity.class)
                .set("id", null)
                .set("title", title)
                .set("company", company)
                .set("city", city)
                .set("district", district)
                .set("employmentType", employmentType)
                .set("experienceLevel", ExperienceLevel.NEWCOMER)
                .set("educationLevel", EducationLevel.HIGH_SCHOOL)
                .set("postedAt", LocalDateTime.now())
                .set("deadline", LocalDateTime.now().plusDays(30))
                .sample();
    }
}
