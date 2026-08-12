package pe.edu.biblioteca.auth.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import pe.edu.biblioteca.common.security.JwtSupport;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {
    @Bean
    SecretKey jwtSecretKey(@Value("${security.jwt.secret}") String secret) {
        return JwtSupport.secretKey(secret);
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey key) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    JwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret) {
        return JwtSupport.decoder(secret);
    }
}
