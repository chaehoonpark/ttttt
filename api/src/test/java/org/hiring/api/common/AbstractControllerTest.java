package org.hiring.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hiring.api.common.exception.GlobalExceptionHandler;
import org.hiring.api.controller.company.CompanyController;
import org.hiring.api.controller.job.JobController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import(GlobalExceptionHandler.class)
public abstract class AbstractControllerTest extends AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
