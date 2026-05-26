package org.example.recruitmentsystem.specification;

import org.example.recruitmentsystem.entity.Application;
import org.example.recruitmentsystem.enumtype.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

public class ApplicationSpecification {

    private ApplicationSpecification() {
    }

    public static Specification<Application> belongsToCandidate(Long candidateId) {
        return (root, query, criteriaBuilder) -> {

            if (candidateId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("candidate").get("id"),
                    candidateId
            );
        };
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        };
    }

    public static Specification<Application> belongsToRecruiterCompany(Long companyId) {
        return (root, query, criteriaBuilder) -> {

            if (companyId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("jobPost").get("company").get("id"),
                    companyId
            );
        };
    }
}