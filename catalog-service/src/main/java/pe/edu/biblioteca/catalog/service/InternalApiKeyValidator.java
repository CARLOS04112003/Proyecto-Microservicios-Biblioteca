package pe.edu.biblioteca.catalog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.edu.biblioteca.common.exception.ForbiddenException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class InternalApiKeyValidator {
    private final byte[] expected;

    public InternalApiKeyValidator(@Value("${app.internal-api-key}") String expected) {
        this.expected = expected.getBytes(StandardCharsets.UTF_8);
    }

    public void validate(String provided) {
        byte[] actual = provided == null ? new byte[0] : provided.getBytes(StandardCharsets.UTF_8);
        if (!MessageDigest.isEqual(expected, actual)) {
            throw new ForbiddenException("Credencial interna inválida");
        }
    }
}
