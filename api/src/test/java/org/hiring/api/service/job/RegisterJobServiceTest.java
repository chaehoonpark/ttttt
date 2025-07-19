package org.hiring.api.service.job;

import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.repository.company.CompanyRepository;
import org.hiring.api.repository.job.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hiring.api.common.testFixture.TestFixture.FM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterJobServiceTest extends AbstractServiceTest {

    @InjectMocks
    private RegisterJobService registerJobService;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("유효한 회사 ID로 채용공고를 등록하면 성공한다")
    void registerJob_WithValidCompanyId_ShouldRegisterJob() {
        // given
        Long companyId = 1L;
        CompanyJpaEntity company = FM.giveMeOne(CompanyJpaEntity.class);
        RegisterJobServiceRequest request = new RegisterJobServiceRequest(
            companyId, "채용공고 제목", "채용공고 설명",
            null, null, null, 3000, 5000,
            null, null, LocalDateTime.now(), LocalDateTime.now(),
            "요구사항", "혜택"
        );

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        // when
        registerJobService.registerJob(request);

        // then
        verify(jobRepository, times(1)).save(any(JobJpaEntity.class));
    }

    @Test
    @DisplayName("존재하지 않는 회사 ID로 채용공고를 등록하려 하면 예외가 발생한다")
    void registerJob_WithInvalidCompanyId_ShouldThrowException() {
        // given
        Long companyId = 999L;
        RegisterJobServiceRequest request = new RegisterJobServiceRequest(
            companyId, null, null, null, null, null,
            null, null, null, null, null, null, null, null
        );

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> registerJobService.registerJob(request))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Company not found with ID: " + companyId);
    }
}