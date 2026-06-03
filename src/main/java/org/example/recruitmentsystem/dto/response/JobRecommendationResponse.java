package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JobRecommendationResponse {

    private JobResponse job;

    private int score;

    private List<String> reasons;
}