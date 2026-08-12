package pe.edu.biblioteca.catalog.api;

import jakarta.validation.constraints.*;

public record BookRequest(
        @NotBlank @Size(max = 20) String isbn,
        @NotBlank @Size(max = 180) String title,
        @NotBlank @Size(max = 140) String author,
        @NotBlank @Size(max = 100) String category,
        @Size(max = 1000) String description,
        @Min(1) @Max(1000) int totalCopies
) {
}
