package org.example.recruitmentsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {

    private String name;

    private String phone;

    private String logoUrl;

    private String bannerUrl;

    private String website;

    private String description;

    private String industry;

    private String address;

    private String companySize;
}