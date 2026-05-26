package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.common.utils.PaginationUtils;
import org.example.recruitmentsystem.dto.request.PaginationRequest;
import org.example.recruitmentsystem.dto.response.SavedJobResponse;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.JobPost;
import org.example.recruitmentsystem.entity.SavedJob;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.SavedJobMapper;
import org.example.recruitmentsystem.repository.CandidateProfileRepository;
import org.example.recruitmentsystem.repository.JobPostRepository;
import org.example.recruitmentsystem.repository.SavedJobRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.service.SavedJobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobPostRepository jobPostRepository;
    private final SavedJobRepository savedJobRepository;
    private final SavedJobMapper savedJobMapper;

    @Override
    public PageResponse<SavedJobResponse> getMySavedJobs(
            String email,
            PaginationRequest request
    ) {
        CandidateProfile candidate = getCandidateProfile(email);

        Pageable pageable = PaginationUtils.buildPageable(request);

        Page<SavedJob> savedJobPage = savedJobRepository.findAll(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(
                                root.get("candidate").get("id"),
                                candidate.getId()
                        ),
                pageable
        );

        return PageResponse.<SavedJobResponse>builder()
                .content(
                        savedJobPage.getContent()
                                .stream()
                                .map(savedJobMapper::toResponse)
                                .toList()
                )
                .currentPage(savedJobPage.getNumber())
                .totalPages(savedJobPage.getTotalPages())
                .totalElements(savedJobPage.getTotalElements())
                .pageSize(savedJobPage.getSize())
                .build();
    }

    @Override
    @Transactional
    public void saveJob(String email, Long jobPostId) {
        CandidateProfile candidate = getCandidateProfile(email);

        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        boolean alreadySaved = savedJobRepository.existsByCandidateAndJobPost(
                candidate,
                jobPost
        );

        if (alreadySaved) {
            return;
        }

        SavedJob savedJob = SavedJob.builder()
                .candidate(candidate)
                .jobPost(jobPost)
                .build();

        savedJobRepository.save(savedJob);
    }

    @Override
    @Transactional
    public void unsaveJob(String email, Long jobPostId) {
        CandidateProfile candidate = getCandidateProfile(email);

        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        savedJobRepository.deleteByCandidateAndJobPost(candidate, jobPost);
    }

    private CandidateProfile getCandidateProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}