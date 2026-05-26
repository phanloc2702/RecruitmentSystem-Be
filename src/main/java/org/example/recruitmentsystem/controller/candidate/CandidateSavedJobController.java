package org.example.recruitmentsystem.controller.candidate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.PaginationRequest;
import org.example.recruitmentsystem.dto.response.SavedJobResponse;
import org.example.recruitmentsystem.service.SavedJobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate/saved-jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateSavedJobController {

    private final SavedJobService savedJobService;

    @GetMapping
    public ApiResponse<PageResponse<SavedJobResponse>> getMySavedJobs(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @ModelAttribute PaginationRequest request
    ) {
        PageResponse<SavedJobResponse> response = savedJobService.getMySavedJobs(
                jwt.getSubject(),
                request
        );

        return ApiResponse.success("Lấy danh sách việc làm đã lưu thành công", response);
    }

    @PostMapping("/{jobPostId}")
    public ApiResponse<Void> saveJob(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long jobPostId
    ) {
        savedJobService.saveJob(jwt.getSubject(), jobPostId);

        return ApiResponse.success("Lưu việc làm thành công");
    }

    @DeleteMapping("/{jobPostId}")
    public ApiResponse<Void> unsaveJob(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long jobPostId
    ) {
        savedJobService.unsaveJob(jwt.getSubject(), jobPostId);

        return ApiResponse.success("Bỏ lưu việc làm thành công");
    }
}