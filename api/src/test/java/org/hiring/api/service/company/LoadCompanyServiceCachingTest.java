package org.hiring.api.service.company;

import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.mapper.CompanyMapper;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.repository.company.query.CompanyQueryRepository;
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

public class LoadCompanyServiceCachingTest extends AbstractServiceTest {

    @Autowired
    private LoadCompanyService loadCompanyService;

    @MockBean
    private CompanyQueryRepository companyQueryRepository;

    @BeforeEach
    void setUp() {
        Cache cache = cacheManager.getCache("companies");
        if (Objects.nonNull(cache)) {
            cache.clear();
        }
    }

    @Test
    @DisplayName("[성공 케이스] 동일한 요청은 캐싱 데이터를 가져온다.")
    void sameRequest_will_bring_from_cached_company_datas() {
        // given - 상황 만들기
        var request1 = createLoadCompaniesRequest("Backend");
        var request2 = createLoadCompaniesRequest("Backend"); // request1과 동일
        var request3 = createLoadCompaniesRequest("Frontend"); // request1과 다름

        given(companyQueryRepository.loadCompanies(any()))
                .willReturn(List.of(createCompany()));
        given(companyQueryRepository.countCompanies(any()))
                .willReturn(1L);

        // when - 동작
        loadCompanyService.loadCompanies(request1);
        loadCompanyService.loadCompanies(request2);
        loadCompanyService.loadCompanies(request3);

        // then - 검증
        verify(companyQueryRepository, times(2)).loadCompanies(any());
        verify(companyQueryRepository, times(2)).countCompanies(any());
    }

    private LoadCompaniesServiceRequest createLoadCompaniesRequest(String keyword) {
        return FM
                .giveMeBuilder(LoadCompaniesServiceRequest.class)
                .set("name", "Test Company")             // ✅ 실제 필드 고정
                .set("location", "Seoul")                // ✅ 실제 필드 고정
                .set("industry", "IT")                   // ✅ 실제 필드 고정
                .set("keywords", List.of(keyword))
                .set("page", 1)
                .set("size", 10)
                .sample();
    }

    private CompanyJpaEntity createCompany() {
        return FM.giveMeBuilder(CompanyJpaEntity.class)
                 .set("id", null)
                 .sample();
    }

}
