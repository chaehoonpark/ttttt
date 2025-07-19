package org.hiring.api.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Table(name = "company")
@DynamicUpdate
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@FieldDefaults(level = PRIVATE)
public class CompanyJpaEntity extends org.example.entity.BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    String name;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    String industry;

    @Column
    String description;

    @Size(max = 50)
    @Column
    String employeeCount;

    @Max(2500)
    @NotNull
    @Column
    Integer foundedYear;

    @Size(max = 200)
    @Column(length = 200)
    String logoUrl;

    @Size(max = 200)
    @Column(length = 200)
    String websiteUrl;

    @NotBlank
    @Size(max = 200)
    @Column(length = 200, nullable = false)
    String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    List<JobJpaEntity> jobs = new ArrayList<>();

    @Builder
    public CompanyJpaEntity(String name, String industry, String description, String employeeCount, Integer foundedYear, String logoUrl, String websiteUrl, String address, List<JobJpaEntity> jobs) {
        this.name = name;
        this.industry = industry;
        this.description = description;
        this.employeeCount = employeeCount;
        this.foundedYear = foundedYear;
        this.logoUrl = logoUrl;
        this.websiteUrl = websiteUrl;
        this.address = address;
        this.jobs = jobs;
    }

    public void modifyCompany(String name, String industry, String description, String employeeCount, Integer foundedYear, String logoUrl, String websiteUrl, String address) {
        this.name = name;
        this.industry = industry;
        this.description = description;
        this.employeeCount = employeeCount;
        this.foundedYear = foundedYear;
        this.logoUrl = logoUrl;
        this.websiteUrl = websiteUrl;
        this.address = address;
    }
}
