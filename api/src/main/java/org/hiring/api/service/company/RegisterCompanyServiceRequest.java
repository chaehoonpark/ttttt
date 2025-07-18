package org.hiring.api.service.company;

public record RegisterCompanyServiceRequest(
    String name,
    String industry,
    String description,
    String employeeCount,
    Integer foundedYear,
    String logoUrl,
    String websiteUrl,
    String address
) {

}
