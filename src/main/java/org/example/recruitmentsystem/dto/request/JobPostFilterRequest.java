package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.EmploymentType;

import java.math.BigDecimal;

@Getter
@Setter
public class JobPostFilterRequest extends PaginationRequest {

    @Size(max = 100, message = "Từ khóa không được vượt quá 100 ký tự")
    private String keyword;

    @Size(max = 100, message = "Địa điểm không được vượt quá 100 ký tự")
    private String location;

    private EmploymentType employmentType;

    private Long categoryId;

    @DecimalMin(value = "0", message = "Lương tối thiểu không hợp lệ")
    private BigDecimal salaryMin;

    @DecimalMin(value = "0", message = "Lương tối đa không hợp lệ")
    private BigDecimal salaryMax;

    @Pattern(
            regexp = "id|title|salaryMin|salaryMax|deadline|createdAt|viewCount",
            message = "Trường sắp xếp không hợp lệ"
    )
    private String sortBy = "createdAt";
}