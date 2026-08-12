package pe.edu.biblioteca.reservation.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_reservations_user_status", columnList = "user_id,status"),
        @Index(name = "idx_reservations_book_status", columnList = "book_id,status")
})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 160)
    private String userEmail;

    @Column(name = "book_id", nullable = false)
    private UUID bookId;

    @Column(nullable = false, length = 180)
    private String bookTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Version
    private long version;

    protected Reservation() {
    }

    public Reservation(UUID userId, String userEmail, UUID bookId, String bookTitle) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.status = ReservationStatus.ACTIVE;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        if (expiresAt == null) expiresAt = createdAt.plus(7, ChronoUnit.DAYS);
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
    public UUID getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public ReservationStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getExpiresAt() { return expiresAt; }

    public void cancel() {
        if (status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("La reserva ya no está activa");
        }
        status = ReservationStatus.CANCELLED;
    }

    public void expireIfNeeded() {
        if (status == ReservationStatus.ACTIVE && expiresAt != null && expiresAt.isBefore(Instant.now())) {
            status = ReservationStatus.EXPIRED;
        }
    }
}
