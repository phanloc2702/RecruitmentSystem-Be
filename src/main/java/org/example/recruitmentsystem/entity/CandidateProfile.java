package org.example.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.recruitmentsystem.enumtype.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // candidate_profiles.user_id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Column(length = 255)
    private String address;

    @Column(name = "current_position", length = 100)
    private String currentPosition;

    @Column(name = "years_of_experience", nullable = false)
    @Builder.Default
    private Integer yearsOfExperience = 0;

    @Column(name = "education_level", length = 100)
    private String educationLevel;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "expected_salary_min")
    private BigDecimal expectedSalaryMin;

    @Column(name = "expected_salary_max")
    private BigDecimal expectedSalaryMax;

    @Column(name = "preferred_location", length = 100)
    private String preferredLocation;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (this.yearsOfExperience == null) {
            this.yearsOfExperience = 0;
        }

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}