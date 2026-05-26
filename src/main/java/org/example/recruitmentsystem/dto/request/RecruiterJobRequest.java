package org.example.recruitmentsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.recruitmentsystem.enumtype.EmploymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RecruiterJobRequest {

    @NotBlank(message = "Tiêu đề việc làm không được để trống")
    @Size(max = 150, message = "Tiêu đề việc làm không được vượt quá 150 ký tự")
    private String title;

    private Long categoryId;

    @NotBlank(message = "Mô tả công việc không được để trống")
    private String description;

    private String requirements;

    private String benefits;

    @Size(max = 100, message = "Địa điểm không được vượt quá 100 ký tự")
    private String location;

    @NotNull(message = "Hình thức làm việc không được để trống")
    private EmploymentType employmentType;

    @Size(max = 50, message = "Cấp bậc kinh nghiệm không được vượt quá 50 ký tự")
    private String experienceLevel;

    @DecimalMin(value = "0", message = "Lương tối thiểu không hợp lệ")
    private BigDecimal salaryMin;

    @DecimalMin(value = "0", message = "Lương tối đa không hợp lệ")
    private BigDecimal salaryMax;

    @Min(value = 1, message = "Số lượng tuyển phải lớn hơn 0")
    private Integer quantity = 1;

    private LocalDate deadline;
}