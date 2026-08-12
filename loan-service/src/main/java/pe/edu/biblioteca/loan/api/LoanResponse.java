package pe.edu.biblioteca.loan.api;

import pe.edu.biblioteca.loan.domain.Loan;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record LoanResponse(
        UUID id,
        UUID userId,
        String userEmail,
        UUID bookId,
        String bookTitle,
        LocalDate borrowedAt,
        LocalDate dueDate,
        LocalDate returnedAt,
        String status,
        Instant createdAt
) {
    public static LoanResponse from(Loan loan) {
        String effectiveStatus = loan.isActive() && loan.getDueDate().isBefore(LocalDate.now())
                ? "OVERDUE" : loan.getStatus().name();
        return new LoanResponse(loan.getId(), loan.getUserId(), loan.getUserEmail(),
                loan.getBookId(), loan.getBookTitle(), loan.getBorrowedAt(), loan.getDueDate(),
                loan.getReturnedAt(), effectiveStatus, loan.getCreatedAt());
    }
}
