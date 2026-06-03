package org.example.recruitmentsystem.service;

import org.example.recruitmentsystem.dto.response.JobRecommendationResponse;

import java.util.List;

public interface JobRecommendationService {

    List<JobRecommendationResponse> recommendForCandidate(String email, int limit);
}