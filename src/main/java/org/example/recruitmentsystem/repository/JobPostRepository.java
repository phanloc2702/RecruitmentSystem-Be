package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long>, JpaSpecificationExecutor<JobPost> {
    @Query("""
        select distinct j.location
        from JobPost j
        where j.location is not null
          and j.location <> ''
          and j.status = org.example.recruitmentsystem.enumtype.JobPostStatus.OPEN
          and j.approvalStatus = org.example.recruitmentsystem.enumtype.ApprovalStatus.APPROVED
        order by j.location asc
        """)
    List<String> findDistinctOpenApprovedLocations();
}