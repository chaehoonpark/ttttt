package org.hiring.api.domain;

import java.time.LocalDateTime;
import java.util.List;

public record Company(
    Long id,
    String name,
    String industry,
    String description,
    String employeeCount,
    Integer foundedYear,
    String logoUrl,
    String websiteUrl,
    String address,
    List<Job> jobs, // 회사가 가진 채용 공고 목록
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
