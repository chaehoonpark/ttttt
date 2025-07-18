package org.hiring.api.repository.job.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EmploymentType;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JobSearchCondition {

    private final String keyword;
    private final CityEnum city;
    private final DistrictEnum district;
    private final EmploymentType employmentType;
    private final int offset;
    private final int limit;
}
