package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.Application;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

    boolean existsByJobPostAndCandidate(JobPost jobPost, CandidateProfile candidate);
}