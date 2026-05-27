package org.example.recruitmentsystem.controller.recruiter;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.dto.request.CompanyRequest;
import org.example.recruitmentsystem.dto.response.CompanyResponse;
import org.example.recruitmentsystem.service.CompanyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/recruiter/company")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterCompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ApiResponse<CompanyResponse> getMyCompany(
            @AuthenticationPrincipal Jwt jwt
    ) {
        CompanyResponse response = companyService.getMyCompany(jwt.getSubject());

        return ApiResponse.success("Lấy thông tin công ty thành công", response);
    }

    @PutMapping
    public ApiResponse<CompanyResponse> updateMyCompany(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CompanyRequest request
    ) {
        CompanyResponse response = companyService.updateMyCompany(jwt.getSubject(), request);

        return ApiResponse.success("Cập nhật thông tin công ty thành công", response);
    }
    @PostMapping("/logo")
    public ApiResponse<CompanyResponse> uploadLogo(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("file") MultipartFile file
    ) {

        CompanyResponse response = companyService.uploadLogo(
                jwt.getSubject(),
                file
        );

        return ApiResponse.success(
                "Tải logo công ty thành công",
                response
        );
    }
}