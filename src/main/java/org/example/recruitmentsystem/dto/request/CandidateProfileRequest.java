package org.example.recruitmentsystem.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CandidateProfileRequest {

    private String fullName;

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
}