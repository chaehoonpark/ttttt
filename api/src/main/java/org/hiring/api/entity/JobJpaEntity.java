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
import org.example.entity.BaseTimeEntity;
import org.hiring.api.entity.enums.CityEnum;
import org.hiring.api.entity.enums.DistrictEnum;
import org.hiring.api.entity.enums.EducationLevel;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.entity.enums.ExperienceLevel;

@Entity
@Getter
@Builder
@Table(name = "job")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class JobJpaEntity extends BaseTimeEntity {

    @Id
    @Column(name = "job_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CompanyJpaEntity company;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column()
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EducationLevel educationLevel;

    @Column()
    private Integer salaryMin;

    @Column()
    private Integer salaryMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CityEnum city; // 근무지 (시/도)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DistrictEnum district; // 근무지 (구/군)

    @Column(nullable = false)
    private LocalDateTime postedAt; // 공고 등록일

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(length = 1000)
    private String requirements; // 자격 요건

    @Column(length = 1000)
    private String benefits; // 복리후생

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
