package org.hiring.api.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Table(name = "company")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class CompanyJpaEntity extends org.example.entity.BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String industry;

    @Lob
    @Column
    private String description;

    @Size(max = 50)
    @Column
    private String employeeCount;

    @Max(2500)
    @NotNull
    @Column
    private Integer foundedYear;

    @Size(max = 200)
    @Column(length = 200)
    private String logoUrl;

    @Size(max = 200)
    @Column(length = 200)
    private String websiteUrl;

    @NotBlank
    @Size(max = 200)
    @Column(length = 200, nullable = false)
    private String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobJpaEntity> jobs = new ArrayList<>();

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