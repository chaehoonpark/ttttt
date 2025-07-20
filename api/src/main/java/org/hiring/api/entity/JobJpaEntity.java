package org.hiring.api.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EducationLevel;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.entity.enums.ExperienceLevel;

@Entity
@Getter
@Builder
@Table(name = "job")
@DynamicUpdate
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class JobJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id",nullable = false)
    CompanyJpaEntity company;

    @Column(nullable = false, length = 255)
    String title;

    @Column()
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    EducationLevel educationLevel;

    @Column()
    Integer salaryMin;

    @Column()
    Integer salaryMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    CityEnum city; // 근무지 (시/도)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    DistrictEnum district; // 근무지 (구/군)

    @Column(nullable = false)
    LocalDateTime postedAt; // 공고 등록일

    @Column(nullable = false)
    LocalDateTime deadline;

    @Column(length = 1000)
    String requirements; // 자격 요건

    @Column(length = 1000)
    String benefits; // 복리후생

    public void updateInfo(
        String title,
        String description,
        CityEnum city,
        DistrictEnum district,
        EmploymentType employmentType,
        ExperienceLevel experienceLevel,
        EducationLevel educationLevel,
        Integer salaryMin,
        Integer salaryMax,
        LocalDateTime postedAt,
        LocalDateTime deadline,
        String requirements,
        String benefits
    ) {
        this.title = title;
        this.description = description;
        this.city = city;
        this.district = district;
        this.employmentType = employmentType;
        this.experienceLevel = experienceLevel;
        this.educationLevel = educationLevel;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.postedAt = postedAt;
        this.deadline = deadline;
        this.requirements = requirements;
        this.benefits = benefits;
    }
}
