package org.example.recruitmentsystem.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.dto.response.CandidateCvResponse;
import org.example.recruitmentsystem.service.CandidateCvService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidate/cvs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateCvController {

    private final CandidateCvService candidateCvService;

    @GetMapping
    public ApiResponse<List<CandidateCvResponse>> getMyCvs(
            @AuthenticationPrincipal Jwt jwt
    ) {
        List<CandidateCvResponse> response = candidateCvService.getMyCvs(jwt.getSubject());

        return ApiResponse.success("Lấy danh sách CV thành công", response);
    }

    @PostMapping
    public ApiResponse<CandidateCvResponse> uploadCv(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file
    ) {
        CandidateCvResponse response = candidateCvService.uploadCv(
                jwt.getSubject(),
                title,
                file
        );

        return ApiResponse.success("Tải CV lên thành công", response);
    }

    @PatchMapping("/{id}/default")
    public ApiResponse<CandidateCvResponse> setDefaultCv(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        CandidateCvResponse response = candidateCvService.setDefaultCv(
                jwt.getSubject(),
                id
        );

        return ApiResponse.success("Đặt CV mặc định thành công", response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCv(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        candidateCvService.deleteCv(jwt.getSubject(), id);

        return ApiResponse.success("Xóa CV thành công");
    }
}