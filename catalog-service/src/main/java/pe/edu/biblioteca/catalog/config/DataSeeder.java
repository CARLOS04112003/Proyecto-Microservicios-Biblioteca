package pe.edu.biblioteca.catalog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.biblioteca.catalog.domain.Book;
import pe.edu.biblioteca.catalog.repository.BookRepository;

@Configuration
public class DataSeeder {
    @Bean
    @ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
    CommandLineRunner seedBooks(BookRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Book("978-612-305-144-6", "Tradiciones peruanas", "Ricardo Palma",
                        "Literatura peruana", "Selección de relatos históricos y costumbristas.", 4));
                repository.save(new Book("978-84-376-0494-7", "Cien años de soledad", "Gabriel García Márquez",
                        "Novela", "Historia de la familia Buendía en Macondo.", 5));
                repository.save(new Book("978-84-206-7420-2", "El principito", "Antoine de Saint-Exupéry",
                        "Narrativa", "Relato clásico sobre la amistad y la mirada esencial.", 6));
                repository.save(new Book("978-013-235088-4", "Clean Code", "Robert C. Martin",
                        "Tecnología", "Buenas prácticas para escribir código mantenible.", 3));
                repository.save(new Book("978-161-729-254-5", "Spring in Action", "Craig Walls",
                        "Tecnología", "Introducción práctica al ecosistema Spring.", 3));
            }
        };
    }
}
