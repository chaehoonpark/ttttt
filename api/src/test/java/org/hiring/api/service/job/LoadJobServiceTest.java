package org.hiring.api.service.job;

import org.example.PagedResult;
import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.domain.Job;
import org.hiring.api.entity.JobJpaEntity;
import org.hiring.api.entity.enums.EducationLevel;
import org.hiring.api.entity.enums.EmploymentType;
import org.hiring.api.entity.enums.ExperienceLevel;
import org.hiring.api.mapper.JobMapper;
import org.hiring.api.repository.job.JobRepository;
import org.hiring.api.repository.job.query.JobQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hiring.api.common.testFixture.TestFixture.FM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoadJobServiceTest extends AbstractServiceTest {

    @Autowired
    private LoadJobService loadJobService;

    @MockBean
    private JobMapper jobMapper;

    @MockBean
    private JobRepository jobRepository;

    @Autowired
    private JobQueryRepository jobQueryRepository;

    @Test
    @DisplayName("[성공 케이스] 존재하는 ID로 채용공고를 조회하면 성공한다")
    void loadJob_WithValidId_ShouldReturnJob() {
        // given
        Long jobId = 1L;
        JobJpaEntity entity = createJobEntity(jobId);
        Job job = FM.giveMeOne(Job.class);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(entity));
        when(jobMapper.toModel(entity)).thenReturn(job);

        // when
        Job result = loadJobService.loadJob(jobId);

        // then
        assertThat(result).isEqualTo(job);
    }

    @Test
    @DisplayName("[실패 케이스] 존재하지 않는 ID로 채용공고를 조회하면 예외가 발생한다")
    void loadJob_WithInvalidId_ShouldThrowException() {
        // given
        Long jobId = 999L;
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> loadJobService.loadJob(jobId)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("[성공 케이스] 조건에 맞는 채용공고 목록을 조회하면 성공한다")
    void loadJobs_WithValidCondition_ShouldReturnPagedResult() {
        // given
        LoadJobsServiceRequest request = createLoadRequest();
        Job job = FM
                .giveMeBuilder(Job.class)
                .set("employmentType", EmploymentType.FULL_TIME)
                .set("experienceLevel", ExperienceLevel.ANY)
                .set("educationLevel", EducationLevel.ANY)
                .sample();

        when(jobMapper.toModel(any(JobJpaEntity.class))).thenReturn(job);

        // when
        PagedResult<Job> result = loadJobService.loadJobs(request);

        // then
        assertThat(result.getContent()).isNotNull();
        assertThat(result.getTotalCount()).isGreaterThanOrEqualTo(0);
    }

    private JobJpaEntity createJobEntity(Long id) {
        return FM.giveMeBuilder(JobJpaEntity.class).set("id", id).sample();
    }

    private LoadJobsServiceRequest createLoadRequest() {
        return new LoadJobsServiceRequest(
                "Java",
                null, // city
                null, // district
                null, // employmentType
                1,
                10
        );
    }
}