package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;
import org.example.recruitmentsystem.enumtype.EmploymentType;
import org.example.recruitmentsystem.enumtype.ExperienceLevel;
import org.example.recruitmentsystem.enumtype.JobPostStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class JobResponse {

    private Long id;

    private Long companyId;

    private String companyName;

    private String companyLogoUrl;

    private Long categoryId;

    private String categoryName;

    private String title;

    private String description;

    private String requirements;

    private String benefits;

    private String location;

    private EmploymentType employmentType;

    private ExperienceLevel experienceLevel;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private Integer quantity;

    private LocalDate deadline;

    private JobPostStatus status;

    private ApprovalStatus approvalStatus;

    private Integer viewCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}