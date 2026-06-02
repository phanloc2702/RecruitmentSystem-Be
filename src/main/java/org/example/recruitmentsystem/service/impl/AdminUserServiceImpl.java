package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.PageResponse;
import org.example.recruitmentsystem.common.utils.PaginationUtils;
import org.example.recruitmentsystem.dto.request.AdminUserFilterRequest;
import org.example.recruitmentsystem.dto.response.AdminUserDetailResponse;
import org.example.recruitmentsystem.dto.response.UserResponse;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.enumtype.UserRole;
import org.example.recruitmentsystem.enumtype.UserStatus;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.ApplicationMapper;
import org.example.recruitmentsystem.mapper.CandidateCvMapper;
import org.example.recruitmentsystem.mapper.CandidateProfileMapper;
import org.example.recruitmentsystem.mapper.CompanyMapper;
import org.example.recruitmentsystem.mapper.JobPostMapper;
import org.example.recruitmentsystem.mapper.UserMapper;
import org.example.recruitmentsystem.repository.ApplicationRepository;
import org.example.recruitmentsystem.repository.CandidateCvRepository;
import org.example.recruitmentsystem.repository.JobPostRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.service.AdminUserService;
import org.example.recruitmentsystem.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CandidateProfileMapper candidateProfileMapper;

    private final CandidateCvMapper candidateCvMapper;

    private final ApplicationMapper applicationMapper;

    private final CompanyMapper companyMapper;

    private final JobPostMapper jobPostMapper;

    private final CandidateCvRepository candidateCvRepository;

    private final ApplicationRepository applicationRepository;

    private final JobPostRepository jobPostRepository;

    @Override
    public PageResponse<UserResponse> getUsers(AdminUserFilterRequest request) {
        Pageable pageable = PaginationUtils.buildPageable(request);

        Page<User> page = userRepository.findAll(
                UserSpecification.adminFilter(request),
                pageable
        );

        Page<UserResponse> responsePage = page.map(userMapper::toResponse);

        return PageResponse.<UserResponse>builder()
                .content(responsePage.getContent())
                .currentPage(responsePage.getNumber())
                .totalPages(responsePage.getTotalPages())
                .totalElements(responsePage.getTotalElements())
                .pageSize(responsePage.getSize())
                .build();
    }

    @Override
    public AdminUserDetailResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        AdminUserDetailResponse.AdminUserDetailResponseBuilder builder =
                AdminUserDetailResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt());

        if (user.getRole() == UserRole.CANDIDATE && user.getCandidateProfile() != null) {
            builder.fullName(user.getCandidateProfile().getFullName());
            builder.phone(user.getCandidateProfile().getPhone());
            builder.avatarUrl(user.getCandidateProfile().getAvatarUrl());

            builder.candidateProfile(
                    candidateProfileMapper.toResponse(user.getCandidateProfile())
            );

            builder.cvs(
                    candidateCvRepository.findByCandidate(user.getCandidateProfile())
                            .stream()
                            .map(candidateCvMapper::toResponse)
                            .toList()
            );

            builder.applications(
                    applicationRepository.findByCandidateOrderByAppliedAtDesc(user.getCandidateProfile())
                            .stream()
                            .map(applicationMapper::toResponse)
                            .toList()
            );
        }

        if (user.getRole() == UserRole.RECRUITER && user.getCompany() != null) {
            builder.fullName(user.getCompany().getName());
            builder.phone(user.getCompany().getPhone());
            builder.avatarUrl(user.getCompany().getLogoUrl());

            builder.company(
                    companyMapper.toResponse(user.getCompany())
            );

            builder.jobs(
                    jobPostRepository.findByCompanyIdOrderByCreatedAtDesc(user.getCompany().getId())
                            .stream()
                            .map(jobPostMapper::toResponse)
                            .toList()
            );
        }

        if (user.getRole() == UserRole.ADMIN) {
            builder.fullName("Quản trị viên");
        }

        return builder.build();
    }

    @Override
    public UserResponse updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.setStatus(status);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }
}