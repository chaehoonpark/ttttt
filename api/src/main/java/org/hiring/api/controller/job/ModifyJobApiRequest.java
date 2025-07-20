// java/org/hiring/api/controller/job/ModifyJobApiRequest.java
package org.hiring.api.controller.job;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EducationLevel;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.entity.enums.ExperienceLevel;
import org.hiring.api.service.job.ModifyJobServiceRequest;

public record ModifyJobApiRequest(
    @NotNull
    Long companyId,

    @NotBlank
    @Size(max = 255)
    String title,

    String description,

    @NotNull
    EmploymentType employmentType,

    @NotNull
    ExperienceLevel experienceLevel,

    @NotNull
    EducationLevel educationLevel,

    @Min(0)
    Integer salaryMin,

    @Min(0)
    Integer salaryMax,

    @NotNull
    CityEnum city,

    @NotNull
    DistrictEnum district,

    @NotNull
    LocalDateTime deadline,

    @NotNull
    LocalDateTime postedAt,

    @Size(max = 1000)
    String requirements,

    @Size(max = 1000)
    String benefits
) {
    public ModifyJobServiceRequest toServiceRequest(Long jobId) {
        return new ModifyJobServiceRequest(
            jobId,
            companyId,
            title,
            description,
            employmentType,
            experienceLevel,
            educationLevel,
            salaryMin,
            salaryMax,
            city,
            district,
            deadline,
            postedAt,
            requirements,
            benefits
        );
    }
}