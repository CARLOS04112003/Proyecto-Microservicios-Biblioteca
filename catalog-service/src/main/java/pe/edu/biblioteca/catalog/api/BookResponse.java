package pe.edu.biblioteca.catalog.api;

import pe.edu.biblioteca.catalog.domain.Book;

import java.time.Instant;
import java.util.UUID;

public record BookResponse(
        UUID id,
        String isbn,
        String title,
        String author,
        String category,
        String description,
        int totalCopies,
        int availableCopies,
        boolean available,
        Instant createdAt,
        Instant updatedAt
) {
    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(),
                book.getCategory(), book.getDescription(), book.getTotalCopies(),
                book.getAvailableCopies(), book.getAvailableCopies() > 0,
                book.getCreatedAt(), book.getUpdatedAt());
    }
}
