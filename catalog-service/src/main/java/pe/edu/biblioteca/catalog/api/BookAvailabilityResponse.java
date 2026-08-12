package pe.edu.biblioteca.catalog.api;

import pe.edu.biblioteca.catalog.domain.Book;

import java.util.UUID;

public record BookAvailabilityResponse(
        UUID bookId,
        String title,
        int totalCopies,
        int availableCopies
) {
    public static BookAvailabilityResponse from(Book book) {
        return new BookAvailabilityResponse(book.getId(), book.getTitle(),
                book.getTotalCopies(), book.getAvailableCopies());
    }
}
