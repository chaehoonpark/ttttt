package org.hiring.api.mapper;

import org.hiring.api.domain.Company;
import org.hiring.api.entity.CompanyJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company toModel(CompanyJpaEntity entity);
}
