package org.rainbow.kinesheet.jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.function.Consumer;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public class JwtGenerator {

    public static final String ISSUER = "http://localhost:9000";
    public static final String AUDIENCE = "kinesheet";

    private final JwtEncoder jwtEncoder;

    public JwtGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generate() {
        return generate(consumer -> {
        });
    }

    public String generate(Consumer<JwtClaimsSet.Builder> consumer) {
        Instant now = Instant.now();
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject("user1")
                .issuer(ISSUER)
                .audience(Arrays.asList(AUDIENCE))
                .claim("email", "user1@company.com")
                .claim("name", "User1");
        consumer.accept(builder);
        JwtEncoderParameters parameters = JwtEncoderParameters.from(builder.build());
        return this.jwtEncoder.encode(parameters).getTokenValue();
    }

}
