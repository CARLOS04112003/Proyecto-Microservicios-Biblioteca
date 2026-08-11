package pe.edu.biblioteca.review;

import org.junit.jupiter.api.Test;
import pe.edu.biblioteca.review.domain.Review;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewDomainTest {
    @Test
    void commentIsTrimmedAndCanBeUpdated() {
        Review review = new Review(UUID.randomUUID(), "user@example.com", UUID.randomUUID(), "Libro", 4, "  Bueno  ");
        assertThat(review.getComment()).isEqualTo("Bueno");
        review.update(5, " Excelente ");
        assertThat(review.getRating()).isEqualTo(5);
        assertThat(review.getComment()).isEqualTo("Excelente");
    }
}
