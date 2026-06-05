package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.dto.response.JobRecommendationResponse;
import org.example.recruitmentsystem.dto.response.JobResponse;
import org.example.recruitmentsystem.entity.Application;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.JobPost;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;
import org.example.recruitmentsystem.enumtype.JobPostStatus;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.JobPostMapper;
import org.example.recruitmentsystem.repository.ApplicationRepository;
import org.example.recruitmentsystem.repository.JobPostRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.service.JobRecommendationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobRecommendationServiceImpl implements JobRecommendationService {

    private final UserRepository userRepository;
    private final JobPostRepository jobPostRepository;
    private final ApplicationRepository applicationRepository;
    private final JobPostMapper jobPostMapper;

    @Override
    public List<JobRecommendationResponse> recommendForCandidate(String email, int limit) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CandidateProfile profile = user.getCandidateProfile();

        if (!isProfileReady(profile)) {
            return List.of();
        }

        Set<Long> appliedJobIds = applicationRepository
                .findByCandidateOrderByAppliedAtDesc(profile)
                .stream()
                .map(Application::getJobPost)
                .map(JobPost::getId)
                .collect(Collectors.toSet());

        List<JobPost> jobs = jobPostRepository.findByStatusAndApprovalStatus(
                JobPostStatus.OPEN,
                ApprovalStatus.APPROVED
        );

        return jobs.stream()
                .filter(job -> !appliedJobIds.contains(job.getId()))
                .map(job -> buildRecommendation(job, profile))
                .filter(item -> item.getScore() >= 20)
                .sorted(Comparator.comparingInt(JobRecommendationResponse::getScore).reversed())
                .limit(limit)
                .toList();
    }

    private JobRecommendationResponse buildRecommendation(JobPost job, CandidateProfile profile) {
        List<String> reasons = new ArrayList<>();

        int score = 0;

        score += scoreTitle(job, profile, reasons);
        score += scoreLocation(job, profile, reasons);
        score += scoreSalary(job, profile, reasons);
        score += scoreExperience(job, profile, reasons);

        if (score > 0) {
            score += scoreFreshness(job, reasons);
            score += scoreDeadline(job, reasons);
        }

        score = Math.min(score, 100);

        JobResponse jobResponse = jobPostMapper.toResponse(job);

        return JobRecommendationResponse.builder()
                .job(jobResponse)
                .score(score)
                .reasons(reasons)
                .build();
    }

    private int scoreTitle(JobPost job, CandidateProfile profile, List<String> reasons) {
        if (!hasText(profile.getCurrentPosition()) || !hasText(job.getTitle())) {
            return 0;
        }

        String position = normalize(profile.getCurrentPosition());
        String title = normalize(job.getTitle());

        if (title.contains(position) || position.contains(title)) {
            reasons.add("Vị trí công việc gần với hồ sơ hiện tại");
            return 30;
        }

        String[] keywords = position.split("\\s+");
        int matchCount = 0;

        for (String keyword : keywords) {
            if (keyword.length() >= 3 && title.contains(keyword)) {
                matchCount++;
            }
        }

        if (matchCount > 0) {
            reasons.add("Có từ khóa nghề nghiệp phù hợp với hồ sơ");
            return Math.min(20, 8 + matchCount * 6);
        }

        return 0;
    }

    private int scoreLocation(JobPost job, CandidateProfile profile, List<String> reasons) {
        if (!hasText(profile.getPreferredLocation()) || !hasText(job.getLocation())) {
            return 0;
        }

        String candidateLocation = normalize(profile.getPreferredLocation());
        String jobLocation = normalize(job.getLocation());

        if (candidateLocation.equals("remote") || jobLocation.equals("remote")) {
            reasons.add("Phù hợp với mong muốn làm việc từ xa");
            return 20;
        }

        if (jobLocation.contains(candidateLocation) || candidateLocation.contains(jobLocation)) {
            reasons.add("Khớp địa điểm mong muốn: " + job.getLocation());
            return 20;
        }

        return 0;
    }

    private int scoreSalary(JobPost job, CandidateProfile profile, List<String> reasons) {
        BigDecimal expectedMin = profile.getExpectedSalaryMin();
        BigDecimal expectedMax = profile.getExpectedSalaryMax();

        BigDecimal jobMin = job.getSalaryMin();
        BigDecimal jobMax = job.getSalaryMax();

        if ((expectedMin == null && expectedMax == null) || (jobMin == null && jobMax == null)) {
            return 0;
        }

        BigDecimal candidateMin = expectedMin != null ? expectedMin : BigDecimal.ZERO;
        BigDecimal candidateMax = expectedMax != null ? expectedMax : new BigDecimal("999999999");

        BigDecimal offerMin = jobMin != null ? jobMin : BigDecimal.ZERO;
        BigDecimal offerMax = jobMax != null ? jobMax : new BigDecimal("999999999");

        boolean overlap = offerMax.compareTo(candidateMin) >= 0
                && offerMin.compareTo(candidateMax) <= 0;

        if (overlap) {
            reasons.add("Mức lương phù hợp với kỳ vọng");
            return 20;
        }

        return 0;
    }

    private int scoreExperience(JobPost job, CandidateProfile profile, List<String> reasons) {
        if (profile.getYearsOfExperience() == null
                || job.getExperienceLevel() == null
                || !hasText(profile.getCurrentPosition())) {
            return 0;
        }

        int years = profile.getYearsOfExperience();
        String level = normalize(job.getExperienceLevel().name());

        if (years == 0 && (level.contains("intern") || level.contains("fresher"))) {
            reasons.add("Phù hợp với ứng viên mới bắt đầu");
            return 15;
        }

        if (years <= 1 && (level.contains("fresher") || level.contains("junior"))) {
            reasons.add("Phù hợp với kinh nghiệm hiện tại");
            return 15;
        }

        if (years <= 3 && (level.contains("junior") || level.contains("middle"))) {
            reasons.add("Phù hợp với kinh nghiệm hiện tại");
            return 15;
        }

        if (years >= 4 && (level.contains("senior") || level.contains("lead") || level.contains("manager"))) {
            reasons.add("Phù hợp với cấp độ kinh nghiệm");
            return 15;
        }

        return 0;
    }

    private int scoreFreshness(JobPost job, List<String> reasons) {
        if (job.getCreatedAt() == null) {
            return 0;
        }

        long days = ChronoUnit.DAYS.between(job.getCreatedAt().toLocalDate(), LocalDate.now());

        if (days <= 7) {
            reasons.add("Tin tuyển dụng mới đăng gần đây");
            return 10;
        }

        if (days <= 30) {
            return 5;
        }

        return 0;
    }

    private int scoreDeadline(JobPost job, List<String> reasons) {
        if (job.getDeadline() == null) {
            return 0;
        }

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), job.getDeadline());

        if (daysLeft < 0) {
            return -100;
        }

        if (daysLeft <= 7) {
            reasons.add("Sắp hết hạn ứng tuyển");
            return 5;
        }

        return 0;
    }

    private boolean isProfileReady(CandidateProfile profile) {
        if (profile == null) {
            return false;
        }

        return hasText(profile.getCurrentPosition())
                || hasText(profile.getPreferredLocation())
                || profile.getYearsOfExperience() != null
                || profile.getExpectedSalaryMin() != null
                || profile.getExpectedSalaryMax() != null;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}