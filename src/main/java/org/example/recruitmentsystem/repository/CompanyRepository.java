package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.Company;
import org.example.recruitmentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Optional<Company> findByRecruiter(User recruiter);

    Optional<Company> findByRecruiterId(Long recruiterId);
}