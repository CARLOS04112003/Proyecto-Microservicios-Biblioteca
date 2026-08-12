package pe.edu.biblioteca.catalog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.biblioteca.catalog.api.BookAvailabilityResponse;
import pe.edu.biblioteca.catalog.api.BookRequest;
import pe.edu.biblioteca.catalog.api.BookResponse;
import pe.edu.biblioteca.catalog.domain.Book;
import pe.edu.biblioteca.catalog.repository.BookRepository;
import pe.edu.biblioteca.common.exception.BadRequestException;
import pe.edu.biblioteca.common.exception.ConflictException;
import pe.edu.biblioteca.common.exception.NotFoundException;

import java.util.UUID;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> search(String query, int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), 100);
        String normalized = query == null ? "" : query.trim();
        return repository.search(normalized,
                        PageRequest.of(Math.max(page, 0), safeSize, Sort.by("title").ascending()))
                .map(BookResponse::from);
    }

    @Transactional(readOnly = true)
    public BookResponse get(UUID id) {
        return BookResponse.from(findActive(id));
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        if (repository.existsByIsbnIgnoreCase(request.isbn().trim())) {
            throw new ConflictException("Ya existe un libro con ese ISBN");
        }
        Book book = new Book(request.isbn(), request.title(), request.author(), request.category(),
                request.description(), request.totalCopies());
        return BookResponse.from(repository.save(book));
    }

    @Transactional
    public BookResponse update(UUID id, BookRequest request) {
        Book book = findActive(id);
        if (repository.existsByIsbnIgnoreCaseAndIdNot(request.isbn().trim(), id)) {
            throw new ConflictException("Ya existe otro libro con ese ISBN");
        }
        try {
            book.update(request.isbn(), request.title(), request.author(), request.category(),
                    request.description(), request.totalCopies());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return BookResponse.from(book);
    }

    @Transactional
    public void delete(UUID id) {
        Book book = findActive(id);
        try {
            book.deactivate();
        } catch (IllegalStateException ex) {
            throw new ConflictException(ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public BookAvailabilityResponse availability(UUID id) {
        return BookAvailabilityResponse.from(findActive(id));
    }

    @Transactional
    public BookAvailabilityResponse checkout(UUID id) {
        Book book = repository.findActiveForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Libro no encontrado"));
        try {
            book.checkout();
        } catch (IllegalStateException ex) {
            throw new ConflictException(ex.getMessage());
        }
        return BookAvailabilityResponse.from(book);
    }

    @Transactional
    public BookAvailabilityResponse checkin(UUID id) {
        Book book = repository.findActiveForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Libro no encontrado"));
        try {
            book.checkin();
        } catch (IllegalStateException ex) {
            throw new ConflictException(ex.getMessage());
        }
        return BookAvailabilityResponse.from(book);
    }

    private Book findActive(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Libro no encontrado"));
    }
}
