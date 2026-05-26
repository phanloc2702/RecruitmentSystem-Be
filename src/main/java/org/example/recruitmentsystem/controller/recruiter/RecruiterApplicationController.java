package org.example.recruitmentsystem.controller.recruiter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.ApplicationFilterRequest;
import org.example.recruitmentsystem.dto.request.UpdateApplicationStatusReques;
import org.example.recruitmentsystem.dto.response.ApplicationResponse;
import org.example.recruitmentsystem.service.ApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/applications")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterApplicationController {

    private final ApplicationService applicationService;

    @GetMapping
    public ApiResponse<PageResponse<ApplicationResponse>> getRecruiterApplications(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @ModelAttribute ApplicationFilterRequest request
    ) {
        PageResponse<ApplicationResponse> response = applicationService.getRecruiterApplications(jwt.getSubject(), request);

        return ApiResponse.success("Lấy danh sách ứng viên ứng tuyển thành công", response);
    }
    @PatchMapping("/{id}/status")
    public ApiResponse<ApplicationResponse> updateApplicationStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatusReques request
    ) {
        ApplicationResponse response = applicationService.updateApplicationStatus(
                jwt.getSubject(),
                id,
                request
        );

        return ApiResponse.success("Cập nhật trạng thái ứng tuyển thành công", response);
    }
}