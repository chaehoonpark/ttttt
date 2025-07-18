package org.hiring.api.service.job;

import lombok.RequiredArgsConstructor;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.repository.job.JobRepository;
import org.hiring.api.service.job.usecase.RemoveJobUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveJobService implements RemoveJobUseCase {

    private final JobRepository jobRepository;

    @Override
    public void removeJob(Long jobId) {
        JobJpaEntity jobJpaEntity = jobRepository.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        jobRepository.delete(jobJpaEntity);
    }
}
