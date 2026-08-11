package pe.edu.biblioteca.review.api;

import pe.edu.biblioteca.review.domain.Review;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        UUID userId,
        String userEmail,
        UUID bookId,
        String bookTitle,
        int rating,
        String comment,
        Instant createdAt,
        Instant updatedAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(review.getId(), review.getUserId(), review.getUserEmail(), review.getBookId(),
                review.getBookTitle(), review.getRating(), review.getComment(), review.getCreatedAt(), review.getUpdatedAt());
    }
}
