package org.example.recruitmentsystem.controller.candidate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.ApplicationFilterRequest;
import org.example.recruitmentsystem.dto.request.ApplicationRequest;
import org.example.recruitmentsystem.dto.response.ApplicationResponse;
import org.example.recruitmentsystem.service.ApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate/applications")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ApiResponse<ApplicationResponse> applyJob(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ApplicationRequest request
    ) {
        ApplicationResponse response = applicationService.applyJob(jwt.getSubject(), request);

        return ApiResponse.success("Ứng tuyển thành công", response);
    }

    @GetMapping
    public ApiResponse<PageResponse<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @ModelAttribute ApplicationFilterRequest request
    ) {
        PageResponse<ApplicationResponse> response = applicationService.getMyApplications(jwt.getSubject(), request);

        return ApiResponse.success("Lấy danh sách ứng tuyển thành công", response);
    }
}