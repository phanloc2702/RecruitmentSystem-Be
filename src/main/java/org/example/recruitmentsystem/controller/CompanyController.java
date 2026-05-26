package org.example.recruitmentsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.dto.request.CompanyRequest;
import org.example.recruitmentsystem.dto.response.CompanyResponse;
import org.example.recruitmentsystem.service.CompanyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/company")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ApiResponse<CompanyResponse> getMyCompany(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getSubject();

        CompanyResponse response = companyService.getMyCompany(email);

        return ApiResponse.success("Lấy thông tin công ty thành công", response);
    }

    @PutMapping
    public ApiResponse<CompanyResponse> updateMyCompany(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CompanyRequest request
    ) {
        String email = jwt.getSubject();

        CompanyResponse response = companyService.updateMyCompany(email, request);

        return ApiResponse.success("Cập nhật thông tin công ty thành công", response);
    }
}