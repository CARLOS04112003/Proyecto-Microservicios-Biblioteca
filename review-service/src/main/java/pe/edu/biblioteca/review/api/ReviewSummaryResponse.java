package pe.edu.biblioteca.review.api;

import java.util.UUID;

public record ReviewSummaryResponse(UUID bookId, long reviews, double averageRating) {
}
