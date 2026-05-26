package org.example.recruitmentsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.recruitmentsystem.dto.request.LoginRequest;
import org.example.recruitmentsystem.dto.request.RegisterRequest;
import org.example.recruitmentsystem.dto.response.AuthResponse;
import org.example.recruitmentsystem.dto.response.UserResponse;
import org.example.recruitmentsystem.entity.CandidateProfile;
import org.example.recruitmentsystem.entity.User;
import org.example.recruitmentsystem.enumtype.UserRole;

import org.example.recruitmentsystem.enumtype.UserStatus;
import org.example.recruitmentsystem.exception.BusinessException;
import org.example.recruitmentsystem.exception.ErrorCode;
import org.example.recruitmentsystem.mapper.UserMapper;
import org.example.recruitmentsystem.repository.CandidateProfileRepository;
import org.example.recruitmentsystem.repository.UserRepository;
import org.example.recruitmentsystem.security.JwtService;
import org.example.recruitmentsystem.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == UserRole.CANDIDATE) {
            CandidateProfile candidateProfile = CandidateProfile.builder()
                    .user(savedUser)
                    .fullName(request.getFullName())
                    .yearsOfExperience(0)
                    .build();

            CandidateProfile savedProfile = candidateProfileRepository.save(candidateProfile);

            // Set ngược lại để UserMapper lấy được fullName/avatar/phone
            savedUser.setCandidateProfile(savedProfile);
        }

        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .user(userMapper.toResponse(savedUser))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.USER_BLOCKED);
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toResponse(user);
    }
}