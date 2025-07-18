package org.hiring.api.repository.job;

import org.hiring.api.entity.JobJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<JobJpaEntity, Long> {

}
