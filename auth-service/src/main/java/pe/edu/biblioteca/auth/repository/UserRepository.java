package pe.edu.biblioteca.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.biblioteca.auth.domain.UserAccount;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
