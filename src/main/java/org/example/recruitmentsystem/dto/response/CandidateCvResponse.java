package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CandidateCvResponse {

    private Long id;

    private Long candidateId;

    private String title;

    private String originalFileName;

    private String fileUrl;

    private String fileType;

    private Long fileSize;

    private String summary;

    private Boolean isDefault;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}