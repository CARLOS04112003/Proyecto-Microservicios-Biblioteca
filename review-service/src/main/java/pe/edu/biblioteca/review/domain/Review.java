package pe.edu.biblioteca.review.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reviews",
        uniqueConstraints = @UniqueConstraint(name = "uk_reviews_user_book", columnNames = {"user_id", "book_id"}),
        indexes = @Index(name = "idx_reviews_book", columnList = "book_id"))
public class Review {
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
    private int rating;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Version
    private long version;

    protected Review() {
    }

    public Review(UUID userId, String userEmail, UUID bookId, String bookTitle, int rating, String comment) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.rating = rating;
        this.comment = normalizeComment(comment);
    }

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public void update(int rating, String comment) {
        this.rating = rating;
        this.comment = normalizeComment(comment);
    }

    private String normalizeComment(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
    public UUID getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
