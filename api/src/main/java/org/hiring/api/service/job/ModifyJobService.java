package org.hiring.api.service.job;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hiring.api.repository.job.JobRepository;
import org.hiring.api.service.job.usecase.ModifyJobUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ModifyJobService implements ModifyJobUseCase {

    private final JobRepository jobRepository;

    @Override
    public void modifyJob(ModifyJobServiceRequest request) {
        final var jobJpaEntity = jobRepository.findById(request.jobId())
            .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + request.jobId()));

        jobJpaEntity.updateInfo(
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
}
