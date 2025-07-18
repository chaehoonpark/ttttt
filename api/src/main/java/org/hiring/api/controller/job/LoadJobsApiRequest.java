package org.hiring.api.controller.job;

import jakarta.validation.constraints.NotNull;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.service.job.LoadJobsServiceRequest;

public record LoadJobsApiRequest(
    String keyword,
    CityEnum city,
    DistrictEnum district,
    EmploymentType employmentType,
    @NotNull Integer page,
    @NotNull Integer size
) {
    public LoadJobsServiceRequest toServiceRequest() {

        return new LoadJobsServiceRequest(
            keyword,
            city,
            district,
            employmentType,
            page,
            size
        );
    }
}