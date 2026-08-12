package pe.edu.biblioteca.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.biblioteca.reservation.domain.Reservation;
import pe.edu.biblioteca.reservation.domain.ReservationStatus;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Reservation> findAllByOrderByCreatedAtDesc();
    List<Reservation> findByUserIdAndStatus(UUID userId, ReservationStatus status);
    List<Reservation> findByStatus(ReservationStatus status);
    long countByUserIdAndStatus(UUID userId, ReservationStatus status);
    boolean existsByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, ReservationStatus status);
}
