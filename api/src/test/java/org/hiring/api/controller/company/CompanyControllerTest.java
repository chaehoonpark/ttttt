package org.hiring.api.controller.company;

import jakarta.persistence.EntityNotFoundException;
import org.hiring.api.common.AbstractControllerTest;
import org.hiring.api.common.testFixture.TestFixtureFactory;
import org.hiring.api.domain.Company;
import org.hiring.api.service.company.LoadCompaniesServiceRequest;
import org.hiring.api.service.company.ModifyCompanyServiceRequest;
import org.hiring.api.service.company.RegisterCompanyServiceRequest;
import org.hiring.api.service.company.usecase.LoadCompanyUseCase;
import org.hiring.api.service.company.usecase.ModifyCompanyUseCase;
import org.hiring.api.service.company.usecase.RegisterCompanyUseCase;
import org.hiring.api.service.company.usecase.RemoveCompanyUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
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


class CompanyControllerTest extends AbstractControllerTest {

    @MockBean
    private RegisterCompanyUseCase registerCompanyUseCase;

    @MockBean
    private LoadCompanyUseCase loadCompanyUseCase;

    @MockBean
    private ModifyCompanyUseCase modifyCompanyUseCase;

    @MockBean
    private RemoveCompanyUseCase removeCompanyUseCase;


    @Nested
    @DisplayName("회사 등록 API (/api/v1/companies) [POST]")
    class RegisterCompany {
        @Test
        @DisplayName("[성공] 유효한 정보로 등록 시 200 OK를 반환한다")
        void success() throws Exception {
            // given
            final var request = fixtureMonkey
                    .giveMeBuilder(RegisterCompanyApiRequest.class)
                    .set("logoUrl", "https://test.com/logo.png")
                    .sample();

            // when
            final var actions = mockMvc.perform(post("/api/v1/companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            actions
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated());

            then(registerCompanyUseCase)
                    .should(times(1))
                    .registerCompany(any(RegisterCompanyServiceRequest.class));
        }

        @DisplayName("[실패] API 명세에 맞지 않는 값으로 요청 시 400 Bad Request를 반환한다")
        @ParameterizedTest(name = "[{index}] 필드: {1} / 케이스: {2}")
        @MethodSource("org.hiring.api.controller.company.CompanyControllerTest#provideInvalidRegisterRequests")
        void fail_withInvalidBody(RegisterCompanyApiRequest request, String field, String caseDesc) throws Exception {
            // when
            ResultActions actions = mockMvc.perform(post("/api/v1/companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            actions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("회사 수정 API (/api/v1/companies/{id}) [PATCH]")
    class ModifyCompany {
        @Test
        @DisplayName("[성공] 유효한 정보로 수정 시 200 OK를 반환한다")
        void success() throws Exception {
            // given
            final var companyId = 1L;
            ModifyCompanyApiRequest request = fixtureMonkey.giveMeOne(ModifyCompanyApiRequest.class);

            // when
            ResultActions actions = mockMvc.perform(patch("/api/v1/companies/" + companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            actions.andExpect(status().isOk());
            verify(modifyCompanyUseCase).modifyCompany(any(ModifyCompanyServiceRequest.class));
        }

        @DisplayName("[실패] API 명세에 맞지 않는 값으로 요청 시 400 Bad Request를 반환한다")
        @ParameterizedTest(name = "[{index}] 필드: {1} / 케이스: {2}")
        @MethodSource("org.hiring.api.controller.company.CompanyControllerTest#provideInvalidModifyRequests")
        void fail_withInvalidBody(ModifyCompanyApiRequest request, String field, String caseDesc) throws Exception {
            // when
            ResultActions actions = mockMvc.perform(patch("/api/v1/companies/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            actions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("회사 삭제 API (/api/v1/companies/{id}) [DELETE]")
    class RemoveCompany {
        @Test
        @DisplayName("[성공] 회사 삭제 시 200 OK를 반환한다")
        void success() throws Exception {
            // given
            Long companyId = 1L;

            // when
            ResultActions actions = mockMvc.perform(delete("/api/v1/companies/" + companyId));

            // then
            actions.andExpect(status().isOk());
            verify(removeCompanyUseCase).removeCompany(companyId);
        }
    }

    @Nested
    @DisplayName("회사 단건 조회 API (/api/v1/companies/{id}) [GET]")
    class LoadCompany {
        @Test
        @DisplayName("[성공] 존재하는 회사 ID로 조회 시 200 OK와 회사 정보를 반환한다")
        void success() throws Exception {
            // given
            Long companyId = 1L;
            Company mockCompany = fixtureMonkey.giveMeBuilder(Company.class).set("id", companyId).sample();
            given(loadCompanyUseCase.loadCompany(companyId)).willReturn(mockCompany);

            // when
            ResultActions actions = mockMvc.perform(get("/api/v1/companies/" + companyId));

            // then
            actions.andExpect(status().isOk()).andExpect(jsonPath("$.data.id").value(companyId));
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 회사 ID로 조회 시 404 Not Found를 반환한다")
        void fail_whenCompanyNotFound() throws Exception {
            // given
            Long notFoundId = 999L;
            given(loadCompanyUseCase.loadCompany(notFoundId)).willThrow(new EntityNotFoundException("Company not found"));

            // when
            ResultActions actions = mockMvc.perform(get("/api/v1/companies/" + notFoundId));

            // then
            actions.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("회사 목록 조회 API (/api/v1/companies) [GET]")
    class LoadCompanies {
        @Test
        @DisplayName("[성공] 유효한 파라미터로 조회 시 200 OK를 반환한다")
        void success() throws Exception {
            // given
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("page", "1");
            params.add("size", "10");

            // when
            ResultActions actions = mockMvc.perform(get("/api/v1/companies").params(params));

            // then
            actions.andExpect(status().isOk());
            verify(loadCompanyUseCase).loadCompanies(any(LoadCompaniesServiceRequest.class));
        }

        @DisplayName("[실패] API 명세에 맞지 않는 페이징 파라미터로 요청 시 400 Bad Request를 반환한다")
        @ParameterizedTest(name = "[{index}] 파라미터: page={1}, size={2}")
        @MethodSource("org.hiring.api.controller.company.CompanyControllerTest#provideInvalidLoadRequests")
        void fail_withInvalidParams(MultiValueMap<String, String> params, String page, String size) throws Exception {
            // when
            ResultActions actions = mockMvc.perform(get("/api/v1/companies").params(params));

            // then
            actions.andExpect(status().isBadRequest());
        }
    }

    // --- MethodSource Providers ---

    static Stream<Arguments> provideInvalidRegisterRequests() {
        final var fixtureMonkey = TestFixtureFactory.getInstance();

        return Stream.of(Arguments.of(fixtureMonkey
                        .giveMeBuilder(RegisterCompanyApiRequest.class)
                        .setNull("name")
                        .sample(), "name", "null"),
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(RegisterCompanyApiRequest.class)
                        .set("name", " ")
                        .sample(), "name", "blank"),
                Arguments.of(fixtureMonkey
                        .giveMeBuilder(RegisterCompanyApiRequest.class)
                        .setNull("foundedYear")
                        .sample(), "foundedYear", "null"));
    }

    static Stream<Arguments> provideInvalidModifyRequests() {
        final var fixtureMonkey = TestFixtureFactory.getInstance();

        return Stream.of(Arguments.of(fixtureMonkey
                .giveMeBuilder(ModifyCompanyApiRequest.class)
                .set("name", "a".repeat(101))
                .sample(), "name", "too long"));
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

        return Stream.of(Arguments.of(params1, "0", "10"),
                Arguments.of(params2, "1", "-1"),
                Arguments.of(params3, null, "10")
        );
    }
}
