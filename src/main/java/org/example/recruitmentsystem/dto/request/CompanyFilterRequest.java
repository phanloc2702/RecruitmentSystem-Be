package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyFilterRequest extends PaginationRequest {

    @Size(max = 100, message = "Từ khóa tìm kiếm không được vượt quá 100 ký tự")
    private String keyword;

    @Size(max = 100, message = "Ngành nghề không được vượt quá 100 ký tự")
    private String industry;

    @Pattern(
            regexp = "id|name|industry|createdAt|updatedAt",
            message = "Trường sắp xếp không hợp lệ"
    )
    private String sortBy = "createdAt";
}