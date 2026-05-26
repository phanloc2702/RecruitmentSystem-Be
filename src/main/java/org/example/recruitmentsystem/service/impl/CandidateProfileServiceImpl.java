package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.dto.request.CandidateProfileRequest;
import org.example.recruitmentsystem.dto.response.CandidateProfileResponse;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.CandidateProfileMapper;
import org.example.recruitmentsystem.repository.CandidateProfileRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.service.CandidateProfileService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final CandidateProfileMapper candidateProfileMapper;

    @Override
    public CandidateProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CandidateProfile profile = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        return candidateProfileMapper.toResponse(profile);
    }

    @Override
    public CandidateProfileResponse updateMyProfile(String email, CandidateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CandidateProfile profile = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        candidateProfileMapper.updateEntityFromRequest(request, profile);

        CandidateProfile savedProfile = candidateProfileRepository.save(profile);

        return candidateProfileMapper.toResponse(savedProfile);
    }
}