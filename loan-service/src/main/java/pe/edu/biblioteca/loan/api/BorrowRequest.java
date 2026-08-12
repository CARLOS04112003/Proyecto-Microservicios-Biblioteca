package pe.edu.biblioteca.loan.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BorrowRequest(
        @NotNull UUID bookId,
        @Min(1) @Max(30) Integer loanDays
) {
    public int effectiveLoanDays() {
        return loanDays == null ? 14 : loanDays;
    }
}
