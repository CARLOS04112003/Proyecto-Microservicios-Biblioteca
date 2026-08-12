package pe.edu.biblioteca.loan.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "loans", indexes = {
        @Index(name = "idx_loans_user_status", columnList = "user_id,status"),
        @Index(name = "idx_loans_book_status", columnList = "book_id,status")
})
public class Loan {
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

    @Column(nullable = false)
    private LocalDate borrowedAt;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Version
    private long version;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
    }

    protected Loan() {
    }

    public Loan(UUID userId, String userEmail, UUID bookId, String bookTitle,
                LocalDate borrowedAt, LocalDate dueDate) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowedAt = borrowedAt;
        this.dueDate = dueDate;
        this.status = LoanStatus.ACTIVE;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
    public UUID getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getBorrowedAt() { return borrowedAt; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnedAt() { return returnedAt; }
    public LoanStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public boolean isActive() { return status == LoanStatus.ACTIVE; }

    public void markReturned(LocalDate date) {
        if (!isActive()) {
            throw new IllegalStateException("El préstamo ya fue devuelto");
        }
        status = LoanStatus.RETURNED;
        returnedAt = date;
    }
}
