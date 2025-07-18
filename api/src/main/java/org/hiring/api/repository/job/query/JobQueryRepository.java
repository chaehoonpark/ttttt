package org.hiring.api.repository.job.query;

import java.util.List;
import org.hiring.api.entity.JobJpaEntity;

public interface JobQueryRepository {

    List<JobJpaEntity> loadJobs(JobSearchCondition condition);
    long countJobs(JobSearchCondition condition);
}
