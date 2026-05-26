package org.example.recruitmentsystem.specification;

import org.example.recruitmentsystem.entity.Company;
import org.example.recruitmentsystem.enumtype.CompanyStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CompanySpecification {

    private CompanySpecification() {
    }

    public static Specification<Company> hasStatus(CompanyStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Company> keywordContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + keyword.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), likePattern)
            );
        };
    }

    public static Specification<Company> industryContains(String industry) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(industry)) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + industry.trim().toLowerCase() + "%";

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("industry")), likePattern);
        };
    }
}