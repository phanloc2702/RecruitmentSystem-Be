package org.example.recruitmentsystem.specification;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.example.recruitmentsystem.dto.request.AdminJobFilterRequest;
import org.example.recruitmentsystem.entity.JobPost;
import org.example.recruitmentsystem.enumtype.ApprovalStatus;
import org.example.recruitmentsystem.enumtype.EmploymentType;
import org.example.recruitmentsystem.enumtype.JobPostStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JobPostSpecification {

    private JobPostSpecification() {
    }

    public static Specification<JobPost> hasApprovedStatus() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("approvalStatus"), ApprovalStatus.APPROVED);
    }

    public static Specification<JobPost> hasOpenStatus() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), JobPostStatus.OPEN);
    }

    public static Specification<JobPost> keywordContains(String keyword) {
        return (root, query, criteriaBuilder) -> {

            if (!StringUtils.hasText(keyword)) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + keyword.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title")),
                            likePattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description")),
                            likePattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("location")),
                            likePattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.join("company", JoinType.LEFT).get("name")
                            ),
                            likePattern
                    )
            );
        };
    }

    public static Specification<JobPost> locationContains(String location) {
        return (root, query, criteriaBuilder) -> {

            if (!StringUtils.hasText(location)) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + location.trim().toLowerCase() + "%";

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")),
                    likePattern
            );
        };
    }

    public static Specification<JobPost> hasEmploymentType(EmploymentType employmentType) {
        return (root, query, criteriaBuilder) -> {

            if (employmentType == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("employmentType"),
                    employmentType
            );
        };
    }

    public static Specification<JobPost> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> {

            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.join("category", JoinType.LEFT).get("id"),
                    categoryId
            );
        };
    }

    public static Specification<JobPost> salaryMinGreaterThanOrEqual(BigDecimal salaryMin) {
        return (root, query, criteriaBuilder) -> {

            if (salaryMin == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("salaryMin"),
                    salaryMin
            );
        };
    }

    public static Specification<JobPost> salaryMaxLessThanOrEqual(BigDecimal salaryMax) {
        return (root, query, criteriaBuilder) -> {

            if (salaryMax == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("salaryMax"),
                    salaryMax
            );
        };
    }
    public static Specification<JobPost> belongsToCompany(Long companyId) {
        return (root, query, criteriaBuilder) -> {
            if (companyId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("company").get("id"),
                    companyId
            );
        };
    }

    public static Specification<JobPost> hasStatus(JobPostStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<JobPost> hasApprovalStatus(ApprovalStatus approvalStatus) {
        return (root, query, criteriaBuilder) -> {
            if (approvalStatus == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("approvalStatus"), approvalStatus);
        };
    }
    public static Specification<JobPost> hasCompanyId(Long companyId) {
        return (root, query, criteriaBuilder) -> {
            if (companyId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("company").get("id"),
                    companyId
            );
        };
    }

    public static Specification<JobPost> recruiterKeywordContains(String keyword) {
        return keywordContains(keyword);
    }
    public static Specification<JobPost> adminFilter(AdminJobFilterRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";

                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("title")),
                                        keyword
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("company").get("name")),
                                        keyword
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("location")),
                                        keyword
                                )
                        )
                );
            }

            if (request.getApprovalStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("approvalStatus"),
                                request.getApprovalStatus()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}