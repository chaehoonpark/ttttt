package org.hiring.api.controller.job;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.BaseResponse;
import org.example.PagedResult;
import org.hiring.api.domain.Job;
import org.hiring.api.service.job.usecase.LoadJobUseCase;
import org.hiring.api.service.job.usecase.ModifyJobUseCase;
import org.hiring.api.service.job.usecase.RegisterJobUseCase;
import org.hiring.api.service.job.usecase.RemoveJobUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final RegisterJobUseCase registerJobUseCase;
    private final LoadJobUseCase loadJobUseCase;
    private final ModifyJobUseCase modifyJobUseCase;
    private final RemoveJobUseCase removeJobUseCase;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> registerJob(
        @Valid @RequestBody RegisterJobApiRequest request
    ) {
        registerJobUseCase.registerJob(request.toServiceRequest());
        return ResponseEntity.ok(BaseResponse.success());
    }

    @PatchMapping("/{jobId}")
    public ResponseEntity<BaseResponse<Void>> modifyJob(
        @PathVariable Long jobId,
        @Valid @RequestBody ModifyJobApiRequest request
    ) {
        modifyJobUseCase.modifyJob(request.toServiceRequest(jobId));
        return ResponseEntity.ok(BaseResponse.success());
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<BaseResponse<Void>> removeJob(
        @PathVariable Long jobId
    ) {
        removeJobUseCase.removeJob(jobId);
        return ResponseEntity.ok(BaseResponse.success());
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<BaseResponse<Job>> loadJob(
        @PathVariable Long jobId
    ) {
        Job job = loadJobUseCase.loadJob(jobId);
        return ResponseEntity.ok(BaseResponse.success(job));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PagedResult<Job>>> loadJobs(
        @Valid LoadJobsApiRequest request
    ) {
        PagedResult<Job> jobs = loadJobUseCase.loadJobs(request.toServiceRequest());
        return ResponseEntity.ok(BaseResponse.success(jobs));
    }
}
