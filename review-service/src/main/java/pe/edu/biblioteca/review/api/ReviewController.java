package pe.edu.biblioteca.review.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pe.edu.biblioteca.review.service.ReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create(@Valid @RequestBody CreateReviewRequest request,
                                 @AuthenticationPrincipal Jwt jwt) {
        return service.create(request, jwt);
    }

    @GetMapping("/me")
    public List<ReviewResponse> myReviews(@AuthenticationPrincipal Jwt jwt) {
        return service.myReviews(jwt);
    }

    @GetMapping("/book/{bookId}")
    public List<ReviewResponse> byBook(@PathVariable UUID bookId) {
        return service.byBook(bookId);
    }

    @GetMapping("/book/{bookId}/summary")
    public ReviewSummaryResponse summary(@PathVariable UUID bookId) {
        return service.summary(bookId);
    }

    @PutMapping("/{id}")
    public ReviewResponse update(@PathVariable UUID id,
                                 @Valid @RequestBody UpdateReviewRequest request,
                                 @AuthenticationPrincipal Jwt jwt) {
        return service.update(id, request, jwt);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        service.delete(id, jwt);
    }
}
