package org.hiring.api.repository.company.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanySearchCondition {

    private final String name;
    private final String description;
    private final String address;
    private final String industry;
    private final int offset;
    private final int limit;
}
