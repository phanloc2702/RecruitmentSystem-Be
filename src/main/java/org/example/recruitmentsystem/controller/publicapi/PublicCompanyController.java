package org.example.recruitmentsystem.controller.publicapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.CompanyFilterRequest;
import org.example.recruitmentsystem.dto.response.CompanyResponse;
import org.example.recruitmentsystem.service.CompanyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class PublicCompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ApiResponse<PageResponse<CompanyResponse>> getCompanies(
            @Valid @ModelAttribute CompanyFilterRequest request
    ) {
        PageResponse<CompanyResponse> response = companyService.getCompanies(request);

        return ApiResponse.success("Lấy danh sách công ty thành công", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> getCompanyById(
            @PathVariable Long id
    ) {
        CompanyResponse response = companyService.getCompanyById(id);

        return ApiResponse.success("Lấy chi tiết công ty thành công", response);
    }
}