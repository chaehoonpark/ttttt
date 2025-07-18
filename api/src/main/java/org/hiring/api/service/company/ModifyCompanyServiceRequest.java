package org.hiring.api.service.company;

public record ModifyCompanyServiceRequest(
    Long companyId,
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
