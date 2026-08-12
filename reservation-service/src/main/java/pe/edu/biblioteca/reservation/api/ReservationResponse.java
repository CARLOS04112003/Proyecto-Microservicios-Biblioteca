package pe.edu.biblioteca.reservation.api;

import pe.edu.biblioteca.reservation.domain.Reservation;

import java.time.Instant;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID userId,
        String userEmail,
        UUID bookId,
        String bookTitle,
        String status,
        Instant createdAt,
        Instant expiresAt
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(), reservation.getUserId(), reservation.getUserEmail(),
                reservation.getBookId(), reservation.getBookTitle(), reservation.getStatus().name(),
                reservation.getCreatedAt(), reservation.getExpiresAt());
    }
}
