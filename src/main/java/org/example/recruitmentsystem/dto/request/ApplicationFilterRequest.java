package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.ApplicationStatus;

@Getter
@Setter
public class ApplicationFilterRequest extends PaginationRequest {

    private ApplicationStatus status;

    @Pattern(
            regexp = "id|status|appliedAt|updatedAt",
            message = "Trường sắp xếp không hợp lệ"
    )
    private String sortBy = "appliedAt";

    @Override
    public String getSafeSortBy() {
        return sortBy == null || sortBy.isBlank() ? "appliedAt" : sortBy;
    }
}