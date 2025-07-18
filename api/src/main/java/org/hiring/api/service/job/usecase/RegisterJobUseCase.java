package org.hiring.api.service.job.usecase;

import org.hiring.api.domain.Job;
import org.hiring.api.service.job.RegisterJobServiceRequest;

public interface RegisterJobUseCase {

    void registerJob(RegisterJobServiceRequest job);
}
