package org.hiring.api.service.company;

import jakarta.persistence.EntityNotFoundException;
import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.repository.company.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hiring.api.common.testFixture.TestFixture.FM;
import static org.mockito.Mockito.*;

class RemoveCompanyServiceTest extends AbstractServiceTest {

    @Autowired
    private RemoveCompanyService removeCompanyService;

    @MockBean
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("[성공 케이스] 존재하는 회사를 삭제하면 성공한다")
    void removeCompany_WithValidCompanyId_ShouldRemoveCompany() {
        // given
        Long companyId = 1L;
        CompanyJpaEntity entity = createCompanyEntity(companyId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(entity));

        // when
        removeCompanyService.removeCompany(companyId);

        // then
        verify(companyRepository, times(1)).findById(companyId);
        verify(companyRepository, times(1)).delete(entity);
    }

    @Test
    @DisplayName("[실패 케이스] 존재하지 않는 회사를 삭제하려 하면 예외가 발생한다")
    void removeCompany_WithInvalidCompanyId_ShouldThrowException() {
        // given
        Long companyId = 999L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> removeCompanyService.removeCompany(companyId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Company not found with id: " + companyId);

        verify(companyRepository, times(1)).findById(companyId);
        verify(companyRepository, never()).delete(any());
    }

    private CompanyJpaEntity createCompanyEntity(Long id) {
        return FM.giveMeBuilder(CompanyJpaEntity.class)
                            .set("id", id)
                            .sample();
    }
}