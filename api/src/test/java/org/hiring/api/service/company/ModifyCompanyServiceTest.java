package org.hiring.api.service.company;

import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.repository.company.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ModifyCompanyServiceTest extends AbstractServiceTest {

    @Autowired
    private ModifyCompanyService modifyCompanyService;

    @MockBean
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
    @DisplayName("[성공 케이스] 존재하는 회사를 수정하면 성공한다")
    void modifyCompany_WithValidRequest_ShouldModifyCompany() {
        // given
        Long companyId = 1L;
        CompanyJpaEntity entity = spy(createCompanyEntity(companyId));
        ModifyCompanyServiceRequest request = createModifyRequest(companyId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(entity));

        // when
        modifyCompanyService.modifyCompany(request);

        // then
        verify(companyRepository, times(1)).findById(companyId);
        verify(entity, times(1)).modifyCompany(
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

    @Test
    @DisplayName("[실패 케이스] 존재하지 않는 회사를 수정하려 하면 예외가 발생한다")
    void modifyCompany_WithInvalidCompanyId_ShouldThrowException() {
        // given
        Long companyId = 999L;
        ModifyCompanyServiceRequest request = createModifyRequest(companyId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> modifyCompanyService.modifyCompany(request))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Company not found with id: " + companyId);

        verify(companyRepository, times(1)).findById(companyId);
    }
}