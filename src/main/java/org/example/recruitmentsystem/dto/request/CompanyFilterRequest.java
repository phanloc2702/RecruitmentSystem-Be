package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyFilterRequest extends PaginationRequest {

    private String keyword;

    private String industry;

    private String location;

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