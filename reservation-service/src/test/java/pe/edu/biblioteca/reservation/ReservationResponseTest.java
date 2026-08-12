package pe.edu.biblioteca.reservation;

import org.junit.jupiter.api.Test;
import pe.edu.biblioteca.reservation.api.ReservationResponse;
import pe.edu.biblioteca.reservation.domain.Reservation;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationResponseTest {
    @Test
    void newReservationIsActive() {
        Reservation reservation = new Reservation(UUID.randomUUID(), "user@example.com", UUID.randomUUID(), "Libro");
        assertThat(ReservationResponse.from(reservation).status()).isEqualTo("ACTIVE");
    }
}
