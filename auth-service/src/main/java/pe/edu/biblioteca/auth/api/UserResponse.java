package pe.edu.biblioteca.auth.api;

import pe.edu.biblioteca.auth.domain.UserAccount;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        String role,
        boolean active,
        Instant createdAt
) {
    public static UserResponse from(UserAccount user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(),
                user.getRole().name(), user.isActive(), user.getCreatedAt());
    }
}
