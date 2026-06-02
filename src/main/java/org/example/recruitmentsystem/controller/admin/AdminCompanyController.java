package org.example.recruitmentsystem.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.AdminCompanyFilterRequest;
import org.example.recruitmentsystem.dto.response.AdminCompanyDetailResponse;
import org.example.recruitmentsystem.dto.response.CompanyResponse;
import org.example.recruitmentsystem.service.AdminCompanyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
public class AdminCompanyController {

    private final AdminCompanyService adminCompanyService;

    @GetMapping
    public ApiResponse<PageResponse<CompanyResponse>> getCompanies(
            AdminCompanyFilterRequest request
    ) {
        return ApiResponse.success(
                "Lấy danh sách công ty thành công",
                adminCompanyService.getCompanies(request)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminCompanyDetailResponse> getCompanyById(
            @PathVariable Long id
    ) {
        return ApiResponse.success(
                "Lấy chi tiết công ty thành công",
                adminCompanyService.getCompanyById(id)
        );
    }
}