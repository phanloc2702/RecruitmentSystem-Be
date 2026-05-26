package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.ApplicationStatus;

@Getter
@Setter
public class UpdateApplicationStatusReques {

    @NotNull(message = "Trạng thái ứng tuyển không được để trống")
    private ApplicationStatus status;
}