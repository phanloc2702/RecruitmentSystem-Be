package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;

@Getter
@Setter
public class UpdateJobApprovalRequest {

    @NotNull(message = "Trạng thái duyệt không được để trống")
    private ApprovalStatus approvalStatus;
}