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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.example.recruitmentsystem.service.FileStorageService;

@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final CandidateProfileMapper candidateProfileMapper;
    private final FileStorageService fileStorageService;
    @Override
    public CandidateProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CandidateProfile profile = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        return toResponseWithAvatarUrl(profile);
    }

    @Override
    public CandidateProfileResponse updateMyProfile(String email, CandidateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CandidateProfile profile = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        candidateProfileMapper.updateEntityFromRequest(request, profile);

        CandidateProfile savedProfile = candidateProfileRepository.save(profile);

        return toResponseWithAvatarUrl(profile);
    }
    @Override
    @Transactional
    public CandidateProfileResponse uploadAvatar(
            String email,
            MultipartFile file
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CandidateProfile profile = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        validateAvatarFile(file);

        String oldObjectName = profile.getAvatarObjectName();

        String newObjectName = fileStorageService.uploadFile(
                file,
                "candidate-avatars"
        );

        profile.setAvatarObjectName(newObjectName);
        profile.setAvatarUrl(null);

        CandidateProfile savedProfile = candidateProfileRepository.save(profile);

        if (oldObjectName != null && !oldObjectName.isBlank()) {
            fileStorageService.deleteFile(oldObjectName);
        }

        return toResponseWithAvatarUrl(profile);
    }
    private void validateAvatarFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        long maxSize = 5 * 1024 * 1024; // 5MB

        if (file.getSize() > maxSize) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/webp"))) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }
    private CandidateProfileResponse toResponseWithAvatarUrl(CandidateProfile profile) {
        CandidateProfileResponse response = candidateProfileMapper.toResponse(profile);

        if (profile.getAvatarObjectName() != null
                && !profile.getAvatarObjectName().isBlank()) {
            response.setAvatarUrl(
                    fileStorageService.getFileUrl(profile.getAvatarObjectName())
            );
        }

        return response;
    }
}