package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.UserRole;
import org.example.recruitmentsystem.enumtype.UserStatus;

@Getter
@Setter
public class AdminUserFilterRequest extends PaginationRequest {

    private String keyword;

    private UserRole role;

    private UserStatus status;

    @Pattern(
            regexp = "id|email|role|status|createdAt|updatedAt",
            message = "Trường sắp xếp không hợp lệ"
    )
    private String sortBy = "createdAt";

    @Override
    public String getSafeSortBy() {
        return sortBy == null || sortBy.isBlank() ? "createdAt" : sortBy;
    }
}