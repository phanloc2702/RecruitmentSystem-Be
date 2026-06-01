package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.UserRole;
import org.example.recruitmentsystem.enumtype.UserStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class AdminUserDetailResponse {

    private Long id;
    private String email;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String fullName;
    private String phone;
    private String avatarUrl;

    // Candidate detail
    private CandidateProfileResponse candidateProfile;
    private List<CandidateCvResponse> cvs;
    private List<ApplicationResponse> applications;

    // Recruiter detail
    private CompanyResponse company;
    private List<JobResponse> jobs;
}