package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;

@Getter
@Setter
public class AdminJobFilterRequest extends PaginationRequest {

    private String keyword;

    private ApprovalStatus approvalStatus;

    @Pattern(
            regexp = "id|title|createdAt|updatedAt|deadline|status|approvalStatus",
            message = "Trường sắp xếp không hợp lệ"
    )
    private String sortBy = "createdAt";

    @Override
    public String getSafeSortBy() {
        return sortBy == null || sortBy.isBlank() ? "createdAt" : sortBy;
    }
}