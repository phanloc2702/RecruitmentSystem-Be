package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.AdminCompanyFilterRequest;
import org.example.recruitmentsystem.dto.response.AdminCompanyDetailResponse;
import org.example.recruitmentsystem.dto.response.CompanyResponse;

public interface AdminCompanyService {

    PageResponse<CompanyResponse> getCompanies(
            AdminCompanyFilterRequest request
    );

    AdminCompanyDetailResponse getCompanyById(Long id);
}