package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.Company;
import org.example.recruitmentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Optional<Company> findByRecruiter(User recruiter);

    Optional<Company> findByRecruiterId(Long recruiterId);

    @Query("""
                select distinct c.industry
                from Company c
                where c.industry is not null
                  and c.industry <> ''
                  and c.status = org.example.recruitmentsystem.enumtype.CompanyStatus.APPROVED
                order by c.industry asc
            """)
    List<String> findDistinctApprovedIndustries();

    @Query("""
                select distinct c.address
                from Company c
                where c.address is not null
                  and c.address <> ''
                  and c.status = org.example.recruitmentsystem.enumtype.CompanyStatus.APPROVED
                order by c.address asc
            """)
    List<String> findDistinctApprovedLocations();
}