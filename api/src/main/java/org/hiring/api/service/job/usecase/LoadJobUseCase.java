package org.hiring.api.service.job.usecase;

import org.hiring.api.common.response.PagedResult;
import org.hiring.api.domain.Job;
import org.hiring.api.service.job.LoadJobsServiceRequest;

public interface LoadJobUseCase {

    Job loadJob(Long jobId);
    PagedResult<Job> loadJobs(LoadJobsServiceRequest request);
}
