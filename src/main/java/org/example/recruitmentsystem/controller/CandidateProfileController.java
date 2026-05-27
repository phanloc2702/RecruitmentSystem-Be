package org.example.recruitmentsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.dto.request.CandidateProfileRequest;
import org.example.recruitmentsystem.dto.response.CandidateProfileResponse;
import org.example.recruitmentsystem.service.CandidateProfileService;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/api/candidate/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    @GetMapping
    public ApiResponse<CandidateProfileResponse> getMyProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getSubject();

        CandidateProfileResponse response = candidateProfileService.getMyProfile(email);

        return ApiResponse.success("Lấy hồ sơ ứng viên thành công", response);
    }

    @PutMapping
    public ApiResponse<CandidateProfileResponse> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CandidateProfileRequest request
    ) {
        String email = jwt.getSubject();

        CandidateProfileResponse response = candidateProfileService.updateMyProfile(email, request);

        return ApiResponse.success("Cập nhật hồ sơ ứng viên thành công", response);
    }
    @PostMapping("/avatar")
    public ApiResponse<CandidateProfileResponse> uploadAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("file") MultipartFile file
    ) {
        String email = jwt.getSubject();

        CandidateProfileResponse response = candidateProfileService.uploadAvatar(
                email,
                file
        );

        return ApiResponse.success(
                "Tải ảnh đại diện thành công",
                response
        );
    }
}