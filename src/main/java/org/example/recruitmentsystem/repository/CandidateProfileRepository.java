package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUser(User user);

    Optional<CandidateProfile> findByUserId(Long userId);
}