package pe.edu.biblioteca.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.biblioteca.loan.domain.Loan;
import pe.edu.biblioteca.loan.domain.LoanStatus;

import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
    List<Loan> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Loan> findAllByOrderByCreatedAtDesc();
    long countByUserIdAndStatus(UUID userId, LoanStatus status);
    boolean existsByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, LoanStatus status);
}
