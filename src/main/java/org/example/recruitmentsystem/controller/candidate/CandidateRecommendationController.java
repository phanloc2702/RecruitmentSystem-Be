package org.example.recruitmentsystem.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.common.ApiResponse;
import org.example.recruitmentsystem.dto.response.JobRecommendationResponse;
import org.example.recruitmentsystem.service.JobRecommendationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate/recommendations")
@RequiredArgsConstructor
public class CandidateRecommendationController {

    private final JobRecommendationService jobRecommendationService;

    @GetMapping
    public ApiResponse<List<JobRecommendationResponse>> recommend(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "6") int limit
    ) {
        return ApiResponse.success(
                "Lấy danh sách việc làm gợi ý thành công",
                jobRecommendationService.recommendForCandidate(
                        jwt.getSubject(),
                        limit
                )
        );
    }
}