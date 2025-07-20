package org.hiring.api.controller.job;


import jakarta.persistence.EntityNotFoundException;
import org.hiring.api.common.AbstractControllerTest;
import org.hiring.api.common.testFixture.TestFixtureFactory;
import org.hiring.api.domain.Job;
import org.hiring.api.service.job.LoadJobsServiceRequest;
import org.hiring.api.service.job.ModifyJobServiceRequest;
import org.hiring.api.service.job.RegisterJobServiceRequest;
import org.hiring.api.service.job.usecase.LoadJobUseCase;
import org.hiring.api.service.job.usecase.ModifyJobUseCase;
import org.hiring.api.service.job.usecase.RegisterJobUseCase;
import org.hiring.api.service.job.usecase.RemoveJobUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
class JobControllerTest extends AbstractControllerTest {

    @MockBean
    private RegisterJobUseCase registerJobUseCase;

    @MockBean
    private LoadJobUseCase loadJobUseCase;

    @MockBean
    private ModifyJobUseCase modifyJobUseCase;

    @MockBean
    private RemoveJobUseCase removeJobUseCase;

    @Nested
    @DisplayName("채용 공고 등록 API (/api/v1/jobs) [POST]")
    class RegisterJob {
        @Test
        @DisplayName("[성공] 유효한 정보로 등록 시 201 Created를 반환한다")
        void success() throws Exception {
            // given
            final var request = fixtureMonkey
                    .giveMeBuilder(RegisterJobApiRequest.class)
                    .set("salaryMin", 3000)
                    .set("salaryMax", 5000)
                    .sample();

            // when
            final var actions = mockMvc.perform(post("/api/v1/jobs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            actions
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());

            then(registerJobUseCase)
                    .should(times(1))
                    .registerJob(any(RegisterJobServiceRequest.class));
        }

        @DisplayName("[실패] API 명세에 맞지 않는 값으로 요청 시 400 Bad Request를 반환한다")
        @ParameterizedTest(name = "[{index}] 필드: {1} / 케이스: {2}")
        @MethodSource("org.hiring.api.controller.job.JobControllerTest#provideInvalidRegisterRequests")
        void fail_withInvalidBody(RegisterJobApiRequest request, String field, String caseDesc) throws Exception {
            // when
            ResultActions actions = mockMvc.perform(post("/api/v1/jobs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            actions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("채용 공고 수정 API (/api/v1/jobs/{id}) [PATCH]")
    class ModifyJob {
        @Test
        @DisplayName("[성공] 유효한 정보로 수정 시 200 OK를 반환한다")
        void success() throws Exception {
            // given
            final var jobId = 1L;
            ModifyJobApiRequest request = fixtureMonkey.giveMeOne(ModifyJobApiRequest.class);

            // when
            ResultActions actions = mockMvc.perform(patch("/api/v1/jobs/" + jobId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            actions.andExpect(status().isOk());
            verify(modifyJobUseCase).modifyJob(any(ModifyJobServiceRequest.class));
        }

        @DisplayName("[실패] API 명세에 맞지 않는 값으로 요청 시 400 Bad Request를 반환한다")
        @ParameterizedTest(name = "[{index}] 필드: {1} / 케이스: {2}")
        @MethodSource("org.hiring.api.controller.job.JobControllerTest#provideInvalidModifyRequests")
        void fail_withInvalidBody(ModifyJobApiRequest request, String field, String caseDesc) throws Exception {
            // when
            ResultActions actions = mockMvc.perform(patch("/api/v1/jobs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            actions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("채용 공고 삭제 API (/api/v1/jobs/{id}) [DELETE]")
    class RemoveJob {
        @Test
        @DisplayName("[성공] 채용 공고 삭제 시 200 OK를 반환한다")
        void success() throws Exception {
            // given
            Long jobId = 1L;

            // when
            ResultActions actions = mockMvc.perform(delete("/api/v1/jobs/" + jobId));

            // then
            actions.andExpect(status().isOk());
            verify(removeJobUseCase).removeJob(jobId);
        }
    }

    @Nested
    @DisplayName("채용 공고 단건 조회 API (/api/v1/jobs/{id}) [GET]")
    class LoadJob {
        @Test
        @DisplayName("[성공] 존재하는 채용 공고 ID로 조회 시 200 OK와 공고 정보를 반환한다")
        void success() throws Exception {
            // given
            Long jobId = 1L;
            Job mockJob = fixtureMonkey.giveMeBuilder(Job.class).set("id", jobId).sample();
            given(loadJobUseCase.loadJob(jobId)).willReturn(mockJob);

            // when
            ResultActions actions = mockMvc.perform(get("/api/v1/jobs/" + jobId));

            // then
            actions.andExpect(status().isOk()).andExpect(jsonPath("$.data.id").value(jobId));
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 채용 공고 ID로 조회 시 404 Not Found를 반환한다")
        void fail_whenJobNotFound() throws Exception {
            // given
            Long notFoundId = 999L;
            given(loadJobUseCase.loadJob(notFoundId)).willThrow(new EntityNotFoundException("Job not found"));

            // when
            ResultActions actions = mockMvc.perform(get("/api/v1/jobs/" + notFoundId));

            // then
            actions.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("채용 공고 목록 조회 API (/api/v1/jobs) [GET]")
    class LoadJobs {
        @Test
        @DisplayName("[성공] 유효한 파라미터로 조회 시 200 OK를 반환한다")
        void success() throws Exception {
            // given
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("page", "1");
            params.add("size", "10");

            // when
            ResultActions actions = mockMvc.perform(get("/api/v1/jobs").params(params));

            // then
            actions.andExpect(status().isOk());
            verify(loadJobUseCase).loadJobs(any(LoadJobsServiceRequest.class));
        }

        @DisplayName("[실패] API 명세에 맞지 않는 페이징 파라미터로 요청 시 400 Bad Request를 반환한다")
        @ParameterizedTest(name = "[{index}] 파라미터: page={1}, size={2}")
        @MethodSource("org.hiring.api.controller.job.JobControllerTest#provideInvalidLoadRequests")
        void fail_withInvalidParams(MultiValueMap<String, String> params, String page, String size) throws Exception {
            // when
            ResultActions actions = mockMvc.perform(get("/api/v1/jobs").params(params));

            // then
            actions.andExpect(status().isBadRequest());
        }
    }

    // --- MethodSource Providers ---

    static Stream<Arguments> provideInvalidRegisterRequests() {
        final var fixtureMonkey = TestFixtureFactory.getInstance();

        return Stream.of(
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(RegisterJobApiRequest.class)
                        .setNull("title")
                        .sample(), "title", "null"),
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(RegisterJobApiRequest.class)
                        .set("title", " ")
                        .sample(), "title", "blank"),
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(RegisterJobApiRequest.class)
                        .setNull("companyId")
                        .sample(), "companyId", "null"),
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(RegisterJobApiRequest.class)
                        .setNull("employmentType")
                        .sample(), "employmentType", "null"),
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(RegisterJobApiRequest.class)
                        .setNull("deadline")
                        .sample(), "deadline", "null")
        );
    }

    static Stream<Arguments> provideInvalidModifyRequests() {
        final var fixtureMonkey = TestFixtureFactory.getInstance();

        return Stream.of(
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(ModifyJobApiRequest.class)
                        .set("title", "a".repeat(256))
                        .sample(), "title", "too long"),
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(ModifyJobApiRequest.class)
                        .set("salaryMin", -1)
                        .sample(), "salaryMin", "negative value"),
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(ModifyJobApiRequest.class)
                        .set("requirements", "a".repeat(1001))
                        .sample(), "requirements", "too long")
        );
    }

    static Stream<Arguments> provideInvalidLoadRequests() {
        final var params1 = new LinkedMultiValueMap<String, String>();
        params1.add("page", "0");
        params1.add("size", "10");

        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("page", "1");
        params2.add("size", "-1");

        final var params3 = new LinkedMultiValueMap<String, String>();
        params3.add("size", "10");

        return Stream.of(
                Arguments.of(params1, "0", "10"),
                Arguments.of(params2, "1", "-1"),
                Arguments.of(params3, null, "10")
        );
    }
}