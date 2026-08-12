package pe.edu.biblioteca.reservation.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.biblioteca.common.exception.ConflictException;
import pe.edu.biblioteca.common.exception.ForbiddenException;
import pe.edu.biblioteca.common.exception.NotFoundException;
import pe.edu.biblioteca.reservation.api.CreateReservationRequest;
import pe.edu.biblioteca.reservation.api.ReservationResponse;
import pe.edu.biblioteca.reservation.client.CatalogBookResponse;
import pe.edu.biblioteca.reservation.client.CatalogClient;
import pe.edu.biblioteca.reservation.domain.Reservation;
import pe.edu.biblioteca.reservation.domain.ReservationStatus;
import pe.edu.biblioteca.reservation.repository.ReservationRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {
    private static final int MAX_ACTIVE_RESERVATIONS = 3;

    private final ReservationRepository repository;
    private final CatalogClient catalogClient;

    public ReservationService(ReservationRepository repository, CatalogClient catalogClient) {
        this.repository = repository;
        this.catalogClient = catalogClient;
    }

    @Transactional
    public ReservationResponse create(CreateReservationRequest request, Jwt jwt) {
        UUID userId = userId(jwt);
        expire(repository.findByUserIdAndStatus(userId, ReservationStatus.ACTIVE));
        if (repository.countByUserIdAndStatus(userId, ReservationStatus.ACTIVE) >= MAX_ACTIVE_RESERVATIONS) {
            throw new ConflictException("El usuario alcanzó el máximo de 3 reservas activas");
        }
        if (repository.existsByUserIdAndBookIdAndStatus(userId, request.bookId(), ReservationStatus.ACTIVE)) {
            throw new ConflictException("El usuario ya tiene una reserva activa para este libro");
        }

        CatalogBookResponse book = catalogClient.getBook(request.bookId());
        Reservation reservation = new Reservation(userId, jwt.getSubject(), book.bookId(), book.title());
        return ReservationResponse.from(repository.save(reservation));
    }

    @Transactional
    public List<ReservationResponse> myReservations(Jwt jwt) {
        List<Reservation> reservations = repository.findByUserIdOrderByCreatedAtDesc(userId(jwt));
        expire(reservations);
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    @Transactional
    public List<ReservationResponse> allReservations() {
        List<Reservation> reservations = repository.findAllByOrderByCreatedAtDesc();
        expire(reservations);
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    @Transactional
    public ReservationResponse cancel(UUID id, Jwt jwt) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));
        if (!isStaff(jwt) && !reservation.getUserId().equals(userId(jwt))) {
            throw new ForbiddenException("No puede cancelar una reserva de otro usuario");
        }
        try {
            reservation.cancel();
        } catch (IllegalStateException ex) {
            throw new ConflictException(ex.getMessage());
        }
        return ReservationResponse.from(reservation);
    }

    private void expire(List<Reservation> reservations) {
        reservations.forEach(Reservation::expireIfNeeded);
    }

    private UUID userId(Jwt jwt) {
        return UUID.fromString(jwt.getClaimAsString("userId"));
    }

    private boolean isStaff(Jwt jwt) {
        String role = jwt.getClaimAsString("role");
        return "ADMIN".equals(role) || "LIBRARIAN".equals(role);
    }
}
