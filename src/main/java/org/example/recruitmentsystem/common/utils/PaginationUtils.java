package org.example.recruitmentsystem.common.utils;

import org.example.recruitmentsystem.dto.request.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    private PaginationUtils() {
    }

    public static Pageable buildPageable(PaginationRequest request) {
        Sort.Direction direction =
                "asc".equalsIgnoreCase(request.getSafeSortDirection())
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, request.getSafeSortBy());

        return PageRequest.of(
                request.getSafePage(),
                request.getSafeSize(),
                sort
        );
    }
}