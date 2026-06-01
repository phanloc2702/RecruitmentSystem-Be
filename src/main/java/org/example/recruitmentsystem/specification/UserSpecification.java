package org.example.recruitmentsystem.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.recruitmentsystem.dto.request.AdminUserFilterRequest;
import org.example.recruitmentsystem.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> adminFilter(AdminUserFilterRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")),
                                keyword
                        )
                );
            }

            if (request.getRole() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("role"),
                                request.getRole()
                        )
                );
            }

            if (request.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                request.getStatus()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}