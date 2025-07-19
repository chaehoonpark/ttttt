package org.hiring.api.service.job;


import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.mapper.CompanyMapper;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.repository.company.query.CompanyQueryRepository;
import org.hiring.api.repository.job.query.JobQueryRepository;
import org.hiring.api.service.company.LoadCompaniesServiceRequest;
import org.hiring.api.service.company.LoadCompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;

import java.util.List;
import java.util.Objects;

import static org.hiring.api.common.testFixture.TestFixture.FM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoadJobServiceCachingTest extends AbstractServiceTest {

    @Autowired
    private LoadJobService loadJobService;

    @MockBean
    private JobQueryRepository jobQueryRepository;

    @BeforeEach
    void setUp() {
        // "jobs" 캐시를 매번 비워서 테스트 독립성 보장
        Cache cache = cacheManager.getCache("jobs");
        if (Objects.nonNull(cache)) {
            cache.clear();
        }
    }

    @Test
    @DisplayName("[성공 케이스] 동일한 채용공고 요청은 캐싱 데이터를 가져온다.")
    void sameJobRequest_will_bring_from_cached_datas() {
        // given - 상황 만들기
        var request1 = createLoadJobsRequest("Backend");
        var request2 = createLoadJobsRequest("Backend"); // request1과 동일
        var request3 = createLoadJobsRequest("Frontend"); // request1과 다름

        // Mock 객체 행동 정의
        given(jobQueryRepository.loadJobs(any())).willReturn(List.of(createJobJpaEntity()));
        given(jobQueryRepository.countJobs(any())).willReturn(1L);

        // when - 동작
        loadJobService.loadJobs(request1); // Cache Miss
        loadJobService.loadJobs(request2); // Cache Hit
        loadJobService.loadJobs(request3); // Cache Miss

        // then - 검증
        verify(jobQueryRepository, times(2)).loadJobs(any());
        verify(jobQueryRepository, times(2)).countJobs(any());
    }

    private LoadJobsServiceRequest createLoadJobsRequest(String keyword) {
        return FM.giveMeBuilder(LoadJobsServiceRequest.class)
                 .set("keyword", keyword)
                 .set("city", CityEnum.SEOUL)
                 .set("district", DistrictEnum.SEOUL_GANGNAM)
                 .set("employmentType", EmploymentType.FULL_TIME)
                 .set("page", 1)
                 .set("size", 10)
                 .sample();
    }

    private JobJpaEntity createJobJpaEntity() {
        return FM.giveMeBuilder(JobJpaEntity.class)
                .set("id", null)
                .sample();
    }
}