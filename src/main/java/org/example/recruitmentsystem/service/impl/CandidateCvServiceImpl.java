package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.dto.response.CandidateCvResponse;
import org.example.recruitmentsystem.entity.CandidateCv;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.CandidateCvMapper;
import org.example.recruitmentsystem.repository.CandidateCvRepository;
import org.example.recruitmentsystem.repository.CandidateProfileRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.service.CandidateCvService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.example.recruitmentsystem.service.FileStorageService;

@Service
@RequiredArgsConstructor
public class CandidateCvServiceImpl implements CandidateCvService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final CandidateCvRepository candidateCvRepository;
    private final CandidateCvMapper candidateCvMapper;
    private final FileStorageService fileStorageService;
    @Override
    public List<CandidateCvResponse> getMyCvs(String email) {
        CandidateProfile candidate = getCandidateProfile(email);

        return candidateCvRepository.findByCandidateIdOrderByCreatedAtDesc(candidate.getId())
                .stream()
                .map(cv -> {
                    CandidateCvResponse response = candidateCvMapper.toResponse(cv);

                    response.setFileUrl(
                            fileStorageService.getFileUrl(
                                    cv.getObjectName()
                            )
                    );

                    return response;
                })
                .toList();
    }

    @Override
    @Transactional
    public CandidateCvResponse uploadCv(
            String email,
            String title,
            MultipartFile file
    ) {
        CandidateProfile candidate = getCandidateProfile(email);

        validateFile(file);

        String originalFileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        Long fileSize = file.getSize();

        String objectName = fileStorageService.uploadFile(file, "candidate-cvs");

        CandidateCv candidateCv = CandidateCv.builder()
                .candidate(candidate)
                .title(title)
                .originalFileName(originalFileName)
                .objectName(objectName)
                .fileType(fileType)
                .fileSize(fileSize)
                .isDefault(false)
                .build();

        if (candidateCvRepository.findByCandidateIdAndIsDefaultTrue(candidate.getId()).isEmpty()) {
            candidateCv.setIsDefault(true);
        }

        CandidateCv savedCv = candidateCvRepository.save(candidateCv);

        CandidateCvResponse response = candidateCvMapper.toResponse(savedCv);
        response.setFileUrl(fileStorageService.getFileUrl(savedCv.getObjectName()));
        return response;
    }

    @Override
    @Transactional
    public CandidateCvResponse setDefaultCv(String email, Long cvId) {
        CandidateProfile candidate = getCandidateProfile(email);

        CandidateCv selectedCv = candidateCvRepository.findByIdAndCandidateId(cvId, candidate.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        List<CandidateCv> cvs = candidateCvRepository.findByCandidate(candidate);

        for (CandidateCv cv : cvs) {
            cv.setIsDefault(false);
        }

        selectedCv.setIsDefault(true);

        candidateCvRepository.saveAll(cvs);

        CandidateCv savedCv = candidateCvRepository.save(selectedCv);

        CandidateCvResponse response = candidateCvMapper.toResponse(savedCv);

        response.setFileUrl(
                fileStorageService.getFileUrl(
                        savedCv.getObjectName()
                )
        );

        return response;
    }

    @Override
    @Transactional
    public void deleteCv(String email, Long cvId) {
        CandidateProfile candidate = getCandidateProfile(email);

        CandidateCv cv = candidateCvRepository.findByIdAndCandidateId(cvId, candidate.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        fileStorageService.deleteFile(
                cv.getObjectName()
        );
        candidateCvRepository.delete(cv);
    }

    private CandidateProfile getCandidateProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        long maxSize = 10 * 1024 * 1024; // 10MB

        if (file.getSize() > maxSize) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("application/pdf")
                        || contentType.equals("application/msword")
                        || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }
}