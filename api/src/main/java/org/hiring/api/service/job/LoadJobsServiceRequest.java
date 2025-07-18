package org.hiring.api.service.job;


import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EmploymentType;

public record LoadJobsServiceRequest(
    String keyword,
    CityEnum city,
    DistrictEnum district,
    EmploymentType employmentType,
    Integer page,
    Integer size
) {

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
