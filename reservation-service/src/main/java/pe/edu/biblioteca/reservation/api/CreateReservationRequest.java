package pe.edu.biblioteca.reservation.api;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateReservationRequest(@NotNull UUID bookId) {
}
