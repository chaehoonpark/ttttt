package org.hiring.api.service.company;

import org.hiring.api.common.AbstractServiceTest;
import org.hiring.api.entity.CompanyJpaEntity;
import org.hiring.api.repository.company.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterCompanyServiceTest extends AbstractServiceTest {

    @Autowired
    private RegisterCompanyService registerCompanyService;

    @MockBean
    private CompanyRepository companyRepository;

    private RegisterCompanyServiceRequest createRegisterRequest() {
        return fixtureMonkey.giveMeOne(RegisterCompanyServiceRequest.class);
    }

    @Test
    @DisplayName("[성공 케이스] 유효한 데이터로 회사를 등록하면 성공한다")
    void registerCompany_WithValidRequest_ShouldRegisterCompany() {
        // given
        RegisterCompanyServiceRequest request = createRegisterRequest();

        // when
        registerCompanyService.registerCompany(request);

        // then
        verify(companyRepository, times(1)).save(any(CompanyJpaEntity.class));
    }
}