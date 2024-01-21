package org.rainbow.kinesheet.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.List;

import org.rainbow.kinesheet.jwt.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@TestConfiguration
public class JwtTestConfiguration {

    @Bean
    JwtEncoder jwtEncoder(@Value("classpath:authz.pub") RSAPublicKey publicKey,
            @Value("classpath:authz.pem") RSAPrivateKey privateKey) {
        RSAKey key = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(key)));
    }

    @Bean
    JwtDecoder jwtDecoder(@Value("classpath:authz.pub") RSAPublicKey publicKey) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(JwtTokenGenerator.ISSUER);
        OAuth2TokenValidator<Jwt> audienceValidator = new JwtClaimValidator<List<Object>>(JwtClaimNames.AUD,
                aud -> !Collections.disjoint(aud, Collections.singleton(JwtTokenGenerator.AUDIENCE)));
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(issuerValidator, audienceValidator));
        return jwtDecoder;
    }

    @Bean
    JwtTokenGenerator jwtTokenGenerator(JwtEncoder JwtEncoder) {
        return new JwtTokenGenerator(JwtEncoder);
    }

}
