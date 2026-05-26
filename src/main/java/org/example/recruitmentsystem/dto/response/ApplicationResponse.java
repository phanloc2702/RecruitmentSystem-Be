package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.ApplicationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApplicationResponse {

    private Long id;

    private Long jobPostId;

    private Long candidateId;

    private CandidateCvResponse cv;

    private String coverLetter;

    private ApplicationStatus status;

    private LocalDateTime appliedAt;

    private LocalDateTime updatedAt;

    private JobResponse job;

    private CandidateProfileResponse candidate;
}