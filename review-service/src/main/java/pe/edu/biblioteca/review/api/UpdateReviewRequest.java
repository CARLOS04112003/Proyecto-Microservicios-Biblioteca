package pe.edu.biblioteca.review.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateReviewRequest(
        @Min(1) @Max(5) int rating,
        @Size(max = 1000) String comment
) {
}
