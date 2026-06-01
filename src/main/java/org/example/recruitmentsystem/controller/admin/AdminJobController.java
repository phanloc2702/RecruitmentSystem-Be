package org.example.recruitmentsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.AdminJobFilterRequest;
import org.example.recruitmentsystem.dto.request.UpdateJobApprovalRequest;
import org.example.recruitmentsystem.dto.response.JobResponse;
import org.example.recruitmentsystem.service.AdminJobService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
public class AdminJobController {

    private final AdminJobService adminJobService;

    @GetMapping
    public ApiResponse<PageResponse<JobResponse>> getJobs(
            @Valid AdminJobFilterRequest request
    ) {
        return ApiResponse.success(
                "Lấy danh sách tin tuyển dụng thành công",
                adminJobService.getJobs(request)
        );
    }

    @PatchMapping("/{id}/approval")
    public ApiResponse<JobResponse> updateApprovalStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobApprovalRequest request
    ) {
        return ApiResponse.success(
                "Cập nhật trạng thái duyệt tin thành công",
                adminJobService.updateApprovalStatus(id, request.getApprovalStatus())
        );
    }
}