package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.CompanyStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CompanyResponse {

    private Long id;

    private Long recruiterId;

    private String name;

    private String phone;

    private String logoUrl;

    private String bannerUrl;

    private String website;

    private String description;

    private String industry;

    private String address;

    private String companySize;

    private CompanyStatus status;

    private Long jobCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}