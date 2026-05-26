package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequest {

    @NotNull(message = "Mã việc làm không được để trống")
    private Long jobPostId;

    private Long cvId;

    private String coverLetter;
}