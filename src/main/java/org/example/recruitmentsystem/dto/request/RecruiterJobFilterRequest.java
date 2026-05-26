package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;
import org.example.recruitmentsystem.enumtype.JobPostStatus;

@Getter
@Setter
public class RecruiterJobFilterRequest extends PaginationRequest {

    @Size(max = 100, message = "Từ khóa không được vượt quá 100 ký tự")
    private String keyword;

    private JobPostStatus status;

    private ApprovalStatus approvalStatus;

    @Pattern(
            regexp = "id|title|status|approvalStatus|deadline|createdAt|updatedAt|viewCount",
            message = "Trường sắp xếp không hợp lệ"
    )
    private String sortBy = "createdAt";
}