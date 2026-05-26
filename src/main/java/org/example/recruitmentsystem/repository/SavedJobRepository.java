package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.JobPost;
import org.example.recruitmentsystem.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long>, JpaSpecificationExecutor<SavedJob> {

    boolean existsByCandidateAndJobPost(CandidateProfile candidate, JobPost jobPost);

    Optional<SavedJob> findByCandidateAndJobPost(CandidateProfile candidate, JobPost jobPost);

    void deleteByCandidateAndJobPost(CandidateProfile candidate, JobPost jobPost);
}