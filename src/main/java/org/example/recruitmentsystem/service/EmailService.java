package org.example.recruitmentsystem.service;

public interface EmailService {

    void sendPasswordResetEmail(
            String to,
            String resetLink
    );

}