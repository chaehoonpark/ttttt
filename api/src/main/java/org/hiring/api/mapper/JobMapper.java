package org.hiring.api.mapper;

import org.hiring.api.domain.Job;
import org.hiring.api.entity.JobJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    Job toModel(JobJpaEntity entity);
}
