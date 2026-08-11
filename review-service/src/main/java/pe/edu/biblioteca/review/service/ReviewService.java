package pe.edu.biblioteca.review.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.biblioteca.common.exception.ConflictException;
import pe.edu.biblioteca.common.exception.ForbiddenException;
import pe.edu.biblioteca.common.exception.NotFoundException;
import pe.edu.biblioteca.review.api.CreateReviewRequest;
import pe.edu.biblioteca.review.api.ReviewResponse;
import pe.edu.biblioteca.review.api.ReviewSummaryResponse;
import pe.edu.biblioteca.review.api.UpdateReviewRequest;
import pe.edu.biblioteca.review.client.CatalogBookResponse;
import pe.edu.biblioteca.review.client.CatalogClient;
import pe.edu.biblioteca.review.domain.Review;
import pe.edu.biblioteca.review.repository.ReviewRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository repository;
    private final CatalogClient catalogClient;

    public ReviewService(ReviewRepository repository, CatalogClient catalogClient) {
        this.repository = repository;
        this.catalogClient = catalogClient;
    }

    @Transactional
    public ReviewResponse create(CreateReviewRequest request, Jwt jwt) {
        UUID userId = userId(jwt);
        if (repository.existsByUserIdAndBookId(userId, request.bookId())) {
            throw new ConflictException("El usuario ya registró una reseña para este libro");
        }
        CatalogBookResponse book = catalogClient.getBook(request.bookId());
        Review review = new Review(userId, jwt.getSubject(), book.bookId(), book.title(), request.rating(), request.comment());
        return ReviewResponse.from(repository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> myReviews(Jwt jwt) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId(jwt)).stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> byBook(UUID bookId) {
        catalogClient.getBook(bookId);
        return repository.findByBookIdOrderByCreatedAtDesc(bookId).stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ReviewSummaryResponse summary(UUID bookId) {
        catalogClient.getBook(bookId);
        List<Review> reviews = repository.findByBookIdOrderByCreatedAtDesc(bookId);
        double avg = reviews.isEmpty() ? 0.0 : reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        double rounded = BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return new ReviewSummaryResponse(bookId, reviews.size(), rounded);
    }

    @Transactional
    public ReviewResponse update(UUID id, UpdateReviewRequest request, Jwt jwt) {
        Review review = find(id);
        if (!review.getUserId().equals(userId(jwt))) {
            throw new ForbiddenException("Solo el autor puede editar su reseña");
        }
        review.update(request.rating(), request.comment());
        return ReviewResponse.from(review);
    }

    @Transactional
    public void delete(UUID id, Jwt jwt) {
        Review review = find(id);
        if (!isStaff(jwt) && !review.getUserId().equals(userId(jwt))) {
            throw new ForbiddenException("No puede eliminar una reseña de otro usuario");
        }
        repository.delete(review);
    }

    private Review find(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Reseña no encontrada"));
    }

    private UUID userId(Jwt jwt) {
        return UUID.fromString(jwt.getClaimAsString("userId"));
    }

    private boolean isStaff(Jwt jwt) {
        String role = jwt.getClaimAsString("role");
        return "ADMIN".equals(role) || "LIBRARIAN".equals(role);
    }
}
