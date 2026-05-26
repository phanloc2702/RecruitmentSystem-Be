package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.CandidateCv;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateCvRepository extends JpaRepository<CandidateCv, Long> {

    List<CandidateCv> findByCandidateIdOrderByCreatedAtDesc(Long candidateId);

    Optional<CandidateCv> findByIdAndCandidateId(Long id, Long candidateId);

    Optional<CandidateCv> findByCandidateIdAndIsDefaultTrue(Long candidateId);

    void deleteByIdAndCandidateId(Long id, Long candidateId);

    List<CandidateCv> findByCandidate(CandidateProfile candidate);
}