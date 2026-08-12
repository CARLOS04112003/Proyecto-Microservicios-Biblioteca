package pe.edu.biblioteca.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.biblioteca.auth.domain.Role;
import pe.edu.biblioteca.auth.domain.UserAccount;
import pe.edu.biblioteca.auth.repository.UserRepository;

@Configuration
public class DataSeeder {
    @Bean
    @ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
    CommandLineRunner seedUsers(UserRepository repository,
                                PasswordEncoder encoder,
                                @Value("${app.seed.admin-password:Admin123*}") String adminPassword,
                                @Value("${app.seed.librarian-password:Library123*}") String librarianPassword,
                                @Value("${app.seed.member-password:Member123*}") String memberPassword) {
        return args -> {
            createIfAbsent(repository, encoder, "Administrador", "admin@biblioteca.pe", adminPassword, Role.ADMIN);
            createIfAbsent(repository, encoder, "Bibliotecario", "bibliotecario@biblioteca.pe", librarianPassword, Role.LIBRARIAN);
            createIfAbsent(repository, encoder, "Usuario Demo", "usuario@biblioteca.pe", memberPassword, Role.MEMBER);
        };
    }

    private void createIfAbsent(UserRepository repository, PasswordEncoder encoder,
                                String name, String email, String password, Role role) {
        if (!repository.existsByEmailIgnoreCase(email)) {
            repository.save(new UserAccount(name, email, encoder.encode(password), role));
        }
    }
}
