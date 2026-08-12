package pe.edu.biblioteca.catalog.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(name = "uk_books_isbn", columnNames = "isbn"))
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(nullable = false, length = 140)
    private String author;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int totalCopies;

    @Column(nullable = false)
    private int availableCopies;

    @Column(nullable = false)
    private boolean active = true;

    @Version
    private long version;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
        updatedAt = createdAt;
        normalize();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
        normalize();
    }

    private void normalize() {
        isbn = isbn.trim();
        title = title.trim();
        author = author.trim();
        category = category.trim();
    }

    protected Book() {
    }

    public Book(String isbn, String title, String author, String category,
                String description, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public UUID getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void update(String isbn, String title, String author, String category,
                       String description, int newTotalCopies) {
        int checkedOut = totalCopies - availableCopies;
        if (newTotalCopies < checkedOut) {
            throw new IllegalArgumentException("El total no puede ser menor que los ejemplares prestados");
        }
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.totalCopies = newTotalCopies;
        this.availableCopies = newTotalCopies - checkedOut;
    }

    public void checkout() {
        if (!active || availableCopies <= 0) {
            throw new IllegalStateException("No hay ejemplares disponibles");
        }
        availableCopies--;
    }

    public void checkin() {
        if (availableCopies >= totalCopies) {
            throw new IllegalStateException("Todos los ejemplares ya están disponibles");
        }
        availableCopies++;
    }

    public void deactivate() {
        if (availableCopies != totalCopies) {
            throw new IllegalStateException("No se puede eliminar un libro con préstamos activos");
        }
        active = false;
    }
}
