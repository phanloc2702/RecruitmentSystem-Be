package org.example.recruitmentsystem.controller.publicapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.JobPostFilterRequest;
import org.example.recruitmentsystem.dto.response.JobCategoryResponse;
import org.example.recruitmentsystem.dto.response.JobResponse;
import org.example.recruitmentsystem.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class PublicJobController {

    private final JobService jobService;

    @GetMapping
    public ApiResponse<PageResponse<JobResponse>> getJobs(
            @Valid @ModelAttribute JobPostFilterRequest request
    ) {
        PageResponse<JobResponse> response = jobService.getJobs(request);

        return ApiResponse.success("Lấy danh sách việc làm thành công", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getJobById(
            @PathVariable Long id
    ) {
        JobResponse response = jobService.getJobById(id);

        return ApiResponse.success("Lấy chi tiết việc làm thành công", response);
    }

    @GetMapping("/categories")
    public ApiResponse<List<JobCategoryResponse>> getCategories() {
        List<JobCategoryResponse> response = jobService.getCategories();

        return ApiResponse.success("Lấy danh mục việc làm thành công", response);
    }
}