package pe.edu.biblioteca.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.biblioteca.review.domain.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByUserIdAndBookId(UUID userId, UUID bookId);
    List<Review> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Review> findByBookIdOrderByCreatedAtDesc(UUID bookId);
}
