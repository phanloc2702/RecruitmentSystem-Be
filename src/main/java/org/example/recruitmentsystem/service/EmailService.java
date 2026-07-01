package org.example.recruitmentsystem.service;

public interface EmailService {

    void sendPasswordResetEmail(
            String to,
            String fullName,
            String resetLink
    );

}