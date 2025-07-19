package org.hiring.api.service.job;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.repository.job.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

class RemoveJobServiceTest extends AbstractServiceTest {

    @InjectMocks
    private RemoveJobService removeJobService;

    @Mock
    private JobRepository jobRepository;

    @Test
    @DisplayName("존재하는 채용공고를 삭제하면 성공한다")
    void removeJob_WithValidJobId_ShouldRemoveJob() {
        // given
        Long jobId = 1L;
        JobJpaEntity entity = JobJpaEntity.builder().build();

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(entity));

        // when
        removeJobService.removeJob(jobId);

        // then
        verify(jobRepository, times(1)).delete(entity);
    }

    @Test
    @DisplayName("존재하지 않는 채용공고를 삭제하려 하면 예외가 발생한다")
    void removeJob_WithInvalidJobId_ShouldThrowException() {
        // given
        Long jobId = 999L;
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> removeJobService.removeJob(jobId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Job not found with id: " + jobId);
    }
}