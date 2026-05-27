package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.dto.request.CandidateProfileRequest;
import org.example.recruitmentsystem.dto.response.CandidateProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CandidateProfileService {

    CandidateProfileResponse getMyProfile(String email);

    CandidateProfileResponse updateMyProfile(String email, CandidateProfileRequest request);

    CandidateProfileResponse uploadAvatar(
            String email,
            MultipartFile file
    );
}