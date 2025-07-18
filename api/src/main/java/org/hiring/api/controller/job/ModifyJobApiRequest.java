package org.hiring.api.controller.job;

import org.hiring.api.service.job.ModifyJobServiceRequest;

public record ModifyJobApiRequest() {

    public ModifyJobServiceRequest toServiceRequest(Long jobId) {
        return null;
    }
}
