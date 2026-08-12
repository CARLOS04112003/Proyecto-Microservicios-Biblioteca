package pe.edu.biblioteca.catalog.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.biblioteca.catalog.domain.Book;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    boolean existsByIsbnIgnoreCase(String isbn);
    boolean existsByIsbnIgnoreCaseAndIdNot(String isbn, UUID id);
    Optional<Book> findByIdAndActiveTrue(UUID id);

    @Query("""
            select b from Book b
            where b.active = true and (
                :query = '' or
                lower(b.title) like lower(concat('%', :query, '%')) or
                lower(b.author) like lower(concat('%', :query, '%')) or
                lower(b.category) like lower(concat('%', :query, '%')) or
                lower(b.isbn) like lower(concat('%', :query, '%'))
            )
            """)
    Page<Book> search(@Param("query") String query, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.id = :id and b.active = true")
    Optional<Book> findActiveForUpdate(@Param("id") UUID id);
}
