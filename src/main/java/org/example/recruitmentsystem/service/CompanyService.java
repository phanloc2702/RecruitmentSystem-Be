package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.dto.request.CompanyFilterRequest;
import org.example.recruitmentsystem.dto.request.CompanyRequest;
import org.example.recruitmentsystem.dto.response.CompanyResponse;

public interface CompanyService {

    CompanyResponse getMyCompany(String email);

    CompanyResponse updateMyCompany(String email, CompanyRequest request);

    PageResponse<CompanyResponse> getCompanies(CompanyFilterRequest request);

    CompanyResponse getCompanyById(Long id);
}