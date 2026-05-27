package org.example.recruitmentsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_ERROR("UNCATEGORIZED_ERROR", "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),

    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER_NOT_FOUND", "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Email hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),

    UNAUTHENTICATED("UNAUTHENTICATED", "Bạn cần đăng nhập", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("UNAUTHORIZED", "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),

    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Không tìm thấy dữ liệu", HttpStatus.NOT_FOUND),
    INVALID_REQUEST("INVALID_REQUEST", "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED("FILE_UPLOAD_FAILED", "Tải file thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_BLOCKED("USER_BLOCKED", "Tài khoản của bạn đã bị khóa", HttpStatus.FORBIDDEN),
    JOB_NOT_OPEN("JOB_NOT_OPEN", "Công việc hiện không còn mở ứng tuyển", HttpStatus.BAD_REQUEST),

    JOB_NOT_APPROVED("JOB_NOT_APPROVED", "Công việc chưa được duyệt nên chưa thể ứng tuyển", HttpStatus.BAD_REQUEST),

    APPLICATION_ALREADY_EXISTS("APPLICATION_ALREADY_EXISTS", "Bạn đã ứng tuyển công việc này rồi", HttpStatus.BAD_REQUEST),

    CV_NOT_FOUND("CV_NOT_FOUND", "CV không tồn tại hoặc không thuộc về bạn", HttpStatus.BAD_REQUEST);
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
