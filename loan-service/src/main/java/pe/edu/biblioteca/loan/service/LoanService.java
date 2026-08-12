package pe.edu.biblioteca.loan.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.biblioteca.common.exception.ConflictException;
import pe.edu.biblioteca.common.exception.ForbiddenException;
import pe.edu.biblioteca.common.exception.NotFoundException;
import pe.edu.biblioteca.loan.api.BorrowRequest;
import pe.edu.biblioteca.loan.api.LoanResponse;
import pe.edu.biblioteca.loan.client.CatalogBookResponse;
import pe.edu.biblioteca.loan.client.CatalogClient;
import pe.edu.biblioteca.loan.domain.Loan;
import pe.edu.biblioteca.loan.domain.LoanStatus;
import pe.edu.biblioteca.loan.repository.LoanRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LoanService {
    private static final int MAX_ACTIVE_LOANS = 5;

    private final LoanRepository repository;
    private final CatalogClient catalogClient;

    public LoanService(LoanRepository repository, CatalogClient catalogClient) {
        this.repository = repository;
        this.catalogClient = catalogClient;
    }

    @Transactional
    public LoanResponse borrow(BorrowRequest request, Jwt jwt) {
        UUID userId = userId(jwt);
        if (repository.countByUserIdAndStatus(userId, LoanStatus.ACTIVE) >= MAX_ACTIVE_LOANS) {
            throw new ConflictException("El usuario alcanzó el máximo de 5 préstamos activos");
        }
        if (repository.existsByUserIdAndBookIdAndStatus(userId, request.bookId(), LoanStatus.ACTIVE)) {
            throw new ConflictException("El usuario ya tiene un préstamo activo de este libro");
        }

        CatalogBookResponse book = catalogClient.checkout(request.bookId());
        LocalDate borrowedAt = LocalDate.now();
        Loan loan = new Loan(userId, jwt.getSubject(), request.bookId(), book.title(),
                borrowedAt, borrowedAt.plusDays(request.effectiveLoanDays()));
        try {
            return LoanResponse.from(repository.save(loan));
        } catch (DataAccessException ex) {
            catalogClient.checkin(request.bookId());
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> myLoans(Jwt jwt) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId(jwt)).stream()
                .map(LoanResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> allLoans() {
        return repository.findAllByOrderByCreatedAtDesc().stream().map(LoanResponse::from).toList();
    }

    @Transactional
    public LoanResponse returnBook(UUID loanId, Jwt jwt) {
        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Préstamo no encontrado"));
        boolean staff = isStaff(jwt);
        if (!staff && !loan.getUserId().equals(userId(jwt))) {
            throw new ForbiddenException("No puede devolver un préstamo de otro usuario");
        }
        if (!loan.isActive()) {
            throw new ConflictException("El préstamo ya fue devuelto");
        }

        catalogClient.checkin(loan.getBookId());
        loan.markReturned(LocalDate.now());
        return LoanResponse.from(loan);
    }

    private UUID userId(Jwt jwt) {
        return UUID.fromString(jwt.getClaimAsString("userId"));
    }

    private boolean isStaff(Jwt jwt) {
        String role = jwt.getClaimAsString("role");
        return "ADMIN".equals(role) || "LIBRARIAN".equals(role);
    }
}
