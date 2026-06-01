package org.example.recruitmentsystem.controller.recruiter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.RecruiterJobFilterRequest;
import org.example.recruitmentsystem.dto.request.RecruiterJobRequest;
import org.example.recruitmentsystem.dto.request.UpdateJobStatusRequest;
import org.example.recruitmentsystem.dto.response.JobResponse;
import org.example.recruitmentsystem.service.JobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterJobController {

    private final JobService jobService;

    @GetMapping
    public ApiResponse<PageResponse<JobResponse>> getMyJobs(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @ModelAttribute RecruiterJobFilterRequest request
    ) {
        PageResponse<JobResponse> response = jobService.getRecruiterJobs(jwt.getSubject(), request);

        return ApiResponse.success("Lấy danh sách việc làm của nhà tuyển dụng thành công", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getMyJobById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        JobResponse response = jobService.getRecruiterJobById(jwt.getSubject(), id);

        return ApiResponse.success("Lấy chi tiết việc làm thành công", response);
    }

    @PostMapping
    public ApiResponse<JobResponse> createJob(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RecruiterJobRequest request
    ) {
        JobResponse response = jobService.createJob(jwt.getSubject(), request);

        return ApiResponse.success("Tạo tin tuyển dụng thành công", response);
    }

    @PutMapping("/{id}")
    public ApiResponse<JobResponse> updateJob(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody RecruiterJobRequest request
    ) {
        JobResponse response = jobService.updateJob(jwt.getSubject(), id, request);

        return ApiResponse.success("Cập nhật tin tuyển dụng thành công", response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteJob(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        jobService.deleteJob(jwt.getSubject(), id);

        return ApiResponse.success("Ẩn tin tuyển dụng thành công");
    }
    @PatchMapping("/{id}/status")
    public ApiResponse<JobResponse> updateStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobStatusRequest request
    ) {
        return ApiResponse.success(
                "Cập nhật trạng thái thành công",
                jobService.updateJobStatus(
                        jwt.getSubject(),
                        id,
                        request.getStatus()
                )
        );
    }
}