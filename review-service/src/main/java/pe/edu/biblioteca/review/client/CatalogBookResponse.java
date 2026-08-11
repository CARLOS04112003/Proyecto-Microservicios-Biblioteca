package pe.edu.biblioteca.review.client;

import java.util.UUID;

public record CatalogBookResponse(UUID bookId, String title, int totalCopies, int availableCopies) {
}
