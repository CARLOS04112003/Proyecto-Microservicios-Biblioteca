package pe.edu.biblioteca.loan;

import org.junit.jupiter.api.Test;
import pe.edu.biblioteca.loan.api.LoanResponse;
import pe.edu.biblioteca.loan.domain.Loan;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LoanResponseTest {
    @Test
    void activePastDueLoanIsReportedAsOverdue() {
        Loan loan = new Loan(UUID.randomUUID(), "user@example.com", UUID.randomUUID(),
                "Libro", LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));
        assertThat(LoanResponse.from(loan).status()).isEqualTo("OVERDUE");
    }
}
