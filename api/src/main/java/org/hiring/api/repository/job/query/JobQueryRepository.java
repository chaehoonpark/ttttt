package org.hiring.api.repository.job.query;

import java.util.List;
import org.hiring.api.entity.JobJpaEntity;
import org.springframework.data.domain.Page;

public interface JobQueryRepository {

    Page<JobJpaEntity> loadJobss(JobSearchCondition condition);
    List<JobJpaEntity> loadJobs(JobSearchCondition condition);
    long countJobs(JobSearchCondition condition);
}
