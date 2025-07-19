package org.hiring.api.service.company;

import org.assertj.core.api.Assertions;
import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.service.company.usecase.ModifyCompanyUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

class ModifyCompanyServiceTest extends AbstractServiceTest {

    @Autowired
    private ModifyCompanyUseCase modifyCompanyUseCase;

    @Autowired
    private CompanyRepository companyRepository;

    private CompanyJpaEntity createCompanyEntity(Long id) {
        return fixtureMonkey.giveMeBuilder(CompanyJpaEntity.class)
            .set("id", id)
            .sample();
    }

    private ModifyCompanyServiceRequest createModifyRequest(Long companyId) {
        return fixtureMonkey.giveMeBuilder(ModifyCompanyServiceRequest.class)
            .set("companyId", companyId)
            .sample();
    }

    @Test
    @Rollback
    @DisplayName("[성공 케이스] 존재하는 회사를 수정하면 성공한다")
    void modifyCompany_WithValidRequest_ShouldModifyCompany() {
        // given
        final var companyEntity = fixtureMonkey.giveMeBuilder(CompanyJpaEntity.class)
                .setNull("id")
                .setNull("jobs")
                .sample();

        final var savedCompanyEntity = companyRepository.save(companyEntity);

        final var request = fixtureMonkey.giveMeBuilder(ModifyCompanyServiceRequest.class)
                .set("companyId", savedCompanyEntity.getId())
                .sample();

        // when
        modifyCompanyUseCase.modifyCompany(request);

        // then
        final var foundCompanyEntity = companyRepository.findById(savedCompanyEntity.getId());

        assertThat(foundCompanyEntity)
                .isNotEmpty()
                .get()
                .extracting(
                        CompanyJpaEntity::getName,
                        CompanyJpaEntity::getIndustry,
                        CompanyJpaEntity::getDescription,
                        CompanyJpaEntity::getEmployeeCount,
                        CompanyJpaEntity::getFoundedYear,
                        CompanyJpaEntity::getLogoUrl,
                        CompanyJpaEntity::getWebsiteUrl,
                        CompanyJpaEntity::getAddress
                ).contains(
                    request.name(),
                    request.industry(),
                    request.description(),
                    request.employeeCount(),
                    request.foundedYear(),
                    request.logoUrl(),
                    request.websiteUrl(),
                    request.address()
                );
    }

//    @Test
//    @DisplayName("[실패 케이스] 존재하지 않는 회사를 수정하려 하면 예외가 발생한다")
//    void modifyCompany_WithInvalidCompanyId_ShouldThrowException() {
//        // given
//        Long companyId = 999L;
//        ModifyCompanyServiceRequest request = createModifyRequest(companyId);
//
//        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());
//
//        // when & then
//        assertThatThrownBy(() -> modifyCompanyService.modifyCompany(request))
//            .isInstanceOf(EntityNotFoundException.class)
//            .hasMessageContaining("Company not found with id: " + companyId);
//
//        verify(companyRepository, times(1)).findById(companyId);
//    }
}
