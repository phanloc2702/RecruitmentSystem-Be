package org.example.recruitmentsystem.repository;

import org.example.recruitmentsystem.entity.PasswordResetToken;
import org.example.recruitmentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(User user);
}