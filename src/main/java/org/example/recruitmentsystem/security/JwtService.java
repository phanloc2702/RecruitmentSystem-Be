package org.example.recruitmentsystem.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.example.recruitmentsystem.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    @Value("${jwt.signerKey}")
    private String signerKey;

    private static final long EXPIRATION_SECONDS = 24 * 60 * 60;

    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(EXPIRATION_SECONDS);

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getEmail())
                .issuer("recruitment-system")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .build();

        return jwtEncoder()
                .encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }

    private JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(signerKey.getBytes()));
    }
}