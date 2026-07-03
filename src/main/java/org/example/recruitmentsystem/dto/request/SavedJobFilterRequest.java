package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedJobFilterRequest extends PaginationRequest {

    @Pattern(
            regexp = "id|savedAt|createdAt",
            message = "Truong sap xep khong hop le"
    )
    private String sortBy = "savedAt";

    @Override
    public String getSafeSortBy() {
        if (sortBy == null || sortBy.isBlank()) {
            return "savedAt";
        }

        if ("createdAt".equals(sortBy)) {
            return "savedAt";
        }

        return sortBy;
    }
}
