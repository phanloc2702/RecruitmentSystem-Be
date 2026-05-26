package org.example.recruitmentsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {

    private UserResponse user;

    private String token;
}
