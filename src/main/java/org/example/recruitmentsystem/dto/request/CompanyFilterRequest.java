package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyFilterRequest {

    @Size(max = 100, message = "Từ khóa tìm kiếm không được vượt quá 100 ký tự")
    private String keyword;

    @Size(max = 100, message = "Ngành nghề không được vượt quá 100 ký tự")
    private String industry;

    @Min(value = 0, message = "Số trang không được nhỏ hơn 0")
    private Integer page = 0;

    @Min(value = 1, message = "Kích thước trang phải lớn hơn 0")
    @Max(value = 100, message = "Kích thước trang không được vượt quá 100")
    private Integer size = 10;

    @Pattern(
            regexp = "id|name|industry|createdAt|updatedAt",
            message = "Trường sắp xếp không hợp lệ"
    )
    private String sortBy = "createdAt";

    @Pattern(
            regexp = "asc|desc|ASC|DESC",
            message = "Kiểu sắp xếp chỉ được là asc hoặc desc"
    )
    private String sortDirection = "desc";

    public int getSafePage() {
        return page == null ? 0 : page;
    }

    public int getSafeSize() {
        return size == null ? 10 : size;
    }

    public String getSafeSortBy() {
        return sortBy == null || sortBy.isBlank() ? "createdAt" : sortBy;
    }

    public String getSafeSortDirection() {
        return sortDirection == null || sortDirection.isBlank() ? "desc" : sortDirection;
    }
}