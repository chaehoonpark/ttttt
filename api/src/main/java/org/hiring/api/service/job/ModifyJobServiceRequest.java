package org.hiring.api.service.job;

import java.time.LocalDateTime;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EducationLevel;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.entity.enums.ExperienceLevel;

public record ModifyJobServiceRequest(
    Long jobId,
    Long companyId,
    String title,
    String description,
    EmploymentType employmentType,
    ExperienceLevel experienceLevel,
    EducationLevel educationLevel,
    Integer salaryMin,
    Integer salaryMax,
    CityEnum city,
    DistrictEnum district,
    LocalDateTime deadline,
    LocalDateTime postedAt,
    String requirements,
    String benefits
) {

}
