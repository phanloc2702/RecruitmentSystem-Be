package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.JobPostStatus;

@Getter
@Setter
public class UpdateJobStatusRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private JobPostStatus status;
}