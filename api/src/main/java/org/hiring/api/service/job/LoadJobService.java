package org.hiring.api.service.job;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.PagedResult;
import org.hiring.api.domain.Job;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.mapper.JobMapper;
import org.hiring.api.repository.job.JobRepository;
import org.hiring.api.repository.job.query.JobQueryRepository;
import org.hiring.api.repository.job.query.JobSearchCondition;
import org.hiring.api.service.job.usecase.LoadJobUseCase;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadJobService implements LoadJobUseCase {

    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final JobQueryRepository jobQueryRepository;

    @Override
    public Job loadJob(final Long jobId) {
        JobJpaEntity entity = jobRepository.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException(""));

        return jobMapper.toModel(entity);
    }

    @Override
    @Cacheable(value = "jobs", key = "#request")
    public PagedResult<Job> loadJobs(final LoadJobsServiceRequest request) {
        JobSearchCondition condition = JobSearchCondition.builder()
            .keyword(request.keyword())
            .city(request.city())
            .district(request.district())
            .employmentType(request.employmentType())
            .limit(request.getLimit())
            .offset(request.getOffset())
            .build();

        List<JobJpaEntity> jobEntities = jobQueryRepository.loadJobs(condition);

        long totalCount = jobQueryRepository.countJobs(condition);

        List<Job> jobs = jobEntities.stream().map(jobMapper::toModel)
            .toList();

        return PagedResult.of(
            jobs,
            request.page(),
            request.size(),
            totalCount
        );
    }
}
