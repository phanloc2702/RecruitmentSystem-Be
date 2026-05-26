package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CandidateProfileResponse {

    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String phone;

    private String avatarUrl;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String address;

    private String currentPosition;

    private Integer yearsOfExperience;

    private String educationLevel;

    private String bio;

    private BigDecimal expectedSalaryMin;

    private BigDecimal expectedSalaryMax;

    private String preferredLocation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}