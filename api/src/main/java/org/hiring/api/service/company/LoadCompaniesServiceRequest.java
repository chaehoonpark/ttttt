package org.hiring.api.service.company;

import java.util.List;

public record LoadCompaniesServiceRequest(
    String name,
    String location,
    String industry,
    List<String> keywords,
    Integer page,
    Integer size
) {

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

    public String getKeywords() {
        StringBuilder result =  new StringBuilder();

        for (String keyword : keywords) {
            result.append(keyword);
        }

        return result.toString();
    }
}
