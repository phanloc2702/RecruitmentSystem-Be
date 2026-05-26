package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
}