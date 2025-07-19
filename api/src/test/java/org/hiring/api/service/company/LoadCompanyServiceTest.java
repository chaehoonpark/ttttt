package org.hiring.api.service.company;

import jakarta.persistence.EntityNotFoundException;
import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.common.response.PagedResult;
import org.hiring.api.domain.Company;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.mapper.CompanyMapper;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.repository.company.query.CompanyQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hiring.api.common.testFixture.TestFixture.FM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoadCompanyServiceTest extends AbstractServiceTest {

    @Autowired
    private LoadCompanyService loadCompanyService;

    @MockBean
    private CompanyMapper companyMapper;

    @MockBean
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyQueryRepository companyQueryRepository;

    @Test
    @DisplayName("[성공 케이스] 존재하는 ID로 회사를 조회하면 성공한다")
    void loadCompany_WithValidId_ShouldReturnCompany() {
        // given
        Long companyId = 1L;
        CompanyJpaEntity entity = createCompanyEntity(companyId);
        Company company = FM.giveMeOne(Company.class);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(entity));
        when(companyMapper.toModel(entity)).thenReturn(company);

        // when
        Company result = loadCompanyService.loadCompany(companyId);

        // then
        assertThat(result).isEqualTo(company);
    }

    @Test
    @DisplayName("[실패 케이스] 존재하지 않는 ID로 회사를 조회하면 예외가 발생한다")
    void loadCompany_WithInvalidId_ShouldThrowException() {
        // given
        Long companyId = 999L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> loadCompanyService.loadCompany(companyId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Company not found with id: " + companyId);
    }

    @Test
    @DisplayName("[성공 케이스] 조건에 맞는 회사 목록을 조회하면 성공한다")
    void loadCompanies_WithValidCondition_ShouldReturnPagedResult() {
        // given
        LoadCompaniesServiceRequest request = createLoadRequest();
        Company company = FM.giveMeBuilder(Company.class)
                                               .set("jobs", null)
                                                       .sample();

        when(companyMapper.toModel(any(CompanyJpaEntity.class)))
                .thenReturn(company);

        // when
        PagedResult<Company> result = loadCompanyService.loadCompanies(request);

        // then
        assertThat(result.getContent()).isNotNull();
        assertThat(result.getTotalCount()).isGreaterThanOrEqualTo(0);
    }

    private CompanyJpaEntity createCompanyEntity(Long id) {
        return FM.giveMeBuilder(CompanyJpaEntity.class)
                            .set("id", id)
                            .sample();
    }

    private LoadCompaniesServiceRequest createLoadRequest() {
        return new LoadCompaniesServiceRequest(
                "테스트회사",
                "서울",
                "IT",
                List.of("Java", "Spring"),
                1,
                10
        );
    }
}
