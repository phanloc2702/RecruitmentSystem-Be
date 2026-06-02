package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCompanyFilterRequest extends PaginationRequest {

    private String keyword;

    @Pattern(
            regexp = "id|name|industry|createdAt|updatedAt",
            message = "Trường sắp xếp không hợp lệ"
    )
    private String sortBy = "createdAt";

    @Override
    public String getSafeSortBy() {
        return sortBy == null || sortBy.isBlank() ? "createdAt" : sortBy;
    }
}