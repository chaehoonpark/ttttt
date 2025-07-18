package org.hiring.api.controller.job;

import org.hiring.api.service.job.RegisterJobServiceRequest;

public record RegisterJobApiRequest() {

    public RegisterJobServiceRequest toServiceRequest() {
        return null;
    }
}
