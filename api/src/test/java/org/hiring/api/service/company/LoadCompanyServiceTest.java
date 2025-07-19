package org.hiring.api.service.company;

import jakarta.persistence.EntityNotFoundException;
import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.domain.Company;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.mapper.CompanyMapper;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.repository.company.query.CompanyQueryRepository;
import org.hiring.api.repository.company.query.CompanySearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class LoadCompanyServiceTest extends AbstractServiceTest {

    @Autowired
    private LoadCompanyService loadCompanyService;

    @SpyBean
    private CompanyMapper companyMapper;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private CompanyQueryRepository companyQueryRepository;

    @Test
    @DisplayName("[성공 케이스] 존재하는 ID로 회사를 조회하면 성공한다")
    void loadCompany_WithValidId_ShouldReturnCompany() {
        // given
        final var companyId = 1L;
        final var companyEntity = fixtureMonkey.giveMeBuilder(CompanyJpaEntity.class)
                .set("id", companyId)
                .sample();

        given(companyRepository.findById(anyLong()))
                .willReturn(Optional.of(companyEntity));

        // when
        final var actualResult = loadCompanyService.loadCompany(companyId);

        // then
        then(companyRepository)
                .should(times(1))
                .findById(anyLong());

        then(companyMapper)
                .should(times(1))
                .toModel(any(CompanyJpaEntity.class));

        assertThat(actualResult)
                .isNotNull()
                .isEqualTo(companyMapper.toModel(companyEntity));
    }

    @Test
    @DisplayName("[실패 케이스] 존재하지 않는 ID로 회사를 조회하면 예외가 발생한다")
    void loadCompany_WithInvalidId_ShouldThrowException() {
        // given
        final var notExistCompanyId = 1L;
        given(companyRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> loadCompanyService.loadCompany(notExistCompanyId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Company not found with id: " + notExistCompanyId);

        then(companyMapper)
                .should(never())
                .toModel(any(CompanyJpaEntity.class));
    }

    @Test
    @DisplayName("[성공 케이스] 조건에 맞는 회사 목록을 조회하면 성공한다")
    void loadCompanies_WithValidCondition_ShouldReturnPagedResult() {
        // given
        final var serviceRequest = fixtureMonkey.giveMeOne(LoadCompaniesServiceRequest.class);
        final var companyEntities = fixtureMonkey.giveMe(CompanyJpaEntity.class, 5);
        final var totalCount = 5;

        final var pagedResult = PageableExecutionUtils.getPage(
                companyEntities,
                Pageable.ofSize(totalCount),
                () -> totalCount
        );

        given(companyQueryRepository.loadCompaniesPage(any(CompanySearchCondition.class)))
                .willReturn(pagedResult);

        // when
        final var actualResult = loadCompanyService.loadCompanies(serviceRequest);

        // then
        then(companyQueryRepository)
                .should(times(1))
                .loadCompaniesPage(any(CompanySearchCondition.class));

        assertThat(actualResult)
                .isNotEmpty()
                .hasSize(totalCount)
                .containsExactlyInAnyOrder(
                        companyEntities.stream()
                                .map(companyMapper::toModel)
                                .toArray(Company[]::new)
                );

    }
}
