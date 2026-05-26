package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SavedJobResponse {

    private Long candidateId;

    private Long jobPostId;

    private LocalDateTime savedAt;

    private JobResponse job;
}