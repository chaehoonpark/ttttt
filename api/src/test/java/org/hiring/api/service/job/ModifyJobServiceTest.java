package org.hiring.api.service.job;

import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.repository.job.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ModifyJobServiceTest extends AbstractServiceTest {

    @InjectMocks
    private ModifyJobService modifyJobService;

    @Mock
    private JobRepository jobRepository;

    @Test
    @DisplayName("존재하는 채용공고를 수정하면 성공한다")
    void modifyJob_WithValidRequest_ShouldModifyJob() {
        // given
        Long jobId = 1L;
        JobJpaEntity entity = spy(fixtureMonkey.giveMeOne(JobJpaEntity.class));
        ModifyJobServiceRequest request = new ModifyJobServiceRequest(
            jobId, 1L, "수정된 제목", "수정된 설명",
            null, null, null, 3000, 5000,
            null, null, LocalDateTime.now(), LocalDateTime.now(),
            "요구사항", "혜택"
        );

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(entity));

        // when
        modifyJobService.modifyJob(request);

        // then
        verify(entity, times(1)).updateInfo(
            request.title(),
            request.description(),
            request.city(),
            request.district(),
            request.employmentType(),
            request.experienceLevel(),
            request.educationLevel(),
            request.salaryMin(),
            request.salaryMax(),
            request.postedAt(),
            request.deadline(),
            request.requirements(),
            request.benefits()
        );
    }

    @Test
    @DisplayName("존재하지 않는 채용공고를 수정하려 하면 예외가 발생한다")
    void modifyJob_WithInvalidJobId_ShouldThrowException() {
        // given
        Long jobId = 999L;
        ModifyJobServiceRequest request = new ModifyJobServiceRequest(
            jobId, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null
        );

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> modifyJobService.modifyJob(request))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Job not found with ID: " + jobId);
    }
}
