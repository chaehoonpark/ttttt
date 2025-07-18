package org.hiring.api.controller.company;

import jakarta.validation.constraints.Min;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.hiring.api.service.company.LoadCompaniesServiceRequest;

public record LoadCompaniesApiRequest(
        String name,
        String location,
        String industry,
        String[] keywords,


        @NotNull(message = "페이지 번호는 필수입니다.")
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        Integer page,

        @NotNull(message = "페이지 크기는 필수입니다.")
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
        Integer size)
{

    public LoadCompaniesServiceRequest toServiceRequest() {
        return new LoadCompaniesServiceRequest(
                name,
                location,
                industry,
                keywords != null ? List.of(keywords) : List.of(),
                page,
                size
        );
    }
}