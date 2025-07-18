package org.hiring.api.domain;

import java.time.LocalDateTime;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EducationLevel;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.entity.enums.ExperienceLevel;

public record Job(
    Long id,
    Long companyId,         // 어느 회사 소속인지 ID로 관리
    String companyName,     // 회사 이름도 함께 보여주면 편리
    String title,
    String description,
    EmploymentType employmentType,
    ExperienceLevel experienceLevel,
    EducationLevel educationLevel,
    Integer salaryMin,
    Integer salaryMax,
    CityEnum city,
    DistrictEnum district,
    LocalDateTime deadlineAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
