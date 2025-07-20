package org.hiring.api.service.job;

import lombok.RequiredArgsConstructor;
import org.hiring.api.common.response.PagedResult;
import org.hiring.api.domain.Job;
import org.hiring.api.mapper.JobMapper;
import org.hiring.api.repository.job.JobRepository;
import org.hiring.api.repository.job.query.JobQueryRepository;
import org.hiring.api.repository.job.query.JobSearchCondition;
import org.hiring.api.service.job.usecase.LoadJobUseCase;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadJobService implements LoadJobUseCase {

    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final JobQueryRepository jobQueryRepository;

    @Override
    public Job loadJob(final Long jobId) {
        final var entity = jobRepository.findById(jobId)
                                        .orElseThrow(() -> new IllegalArgumentException(""));

        return jobMapper.toModel(entity);
    }

    @Override
    @Cacheable(value = "jobs", key = "#request")
    public PagedResult<Job> loadJobs(final LoadJobsServiceRequest request) {
        final var condition = JobSearchCondition.builder()
                                                .keyword(request.keyword())
                                                .city(request.city())
                                                .district(request.district())
                                                .employmentType(request.employmentType())
                                                .limit(request.getLimit())
                                                .offset(request.getOffset())
                                                .build();

        final var jobEntities = jobQueryRepository.loadJobss(condition);

        final var jobs = jobEntities.stream().map(jobMapper::toModel)
                                    .toList();

        return PagedResult.of(
                jobs,
                request.page(),
                request.size(),
                jobEntities.getTotalElements()
        );
    }
}
