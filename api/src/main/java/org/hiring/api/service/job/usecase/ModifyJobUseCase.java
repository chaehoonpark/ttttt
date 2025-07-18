package org.hiring.api.service.job.usecase;

import org.hiring.api.domain.Job;
import org.hiring.api.service.job.ModifyJobServiceRequest;

public interface ModifyJobUseCase {

    void modifyJob(ModifyJobServiceRequest request);
}
