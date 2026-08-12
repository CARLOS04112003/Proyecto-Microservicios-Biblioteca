package pe.edu.biblioteca.loan.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pe.edu.biblioteca.loan.service.LoanService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse borrow(@Valid @RequestBody BorrowRequest request,
                               @AuthenticationPrincipal Jwt jwt) {
        return service.borrow(request, jwt);
    }

    @GetMapping("/me")
    public List<LoanResponse> myLoans(@AuthenticationPrincipal Jwt jwt) {
        return service.myLoans(jwt);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public List<LoanResponse> allLoans() {
        return service.allLoans();
    }

    @PostMapping("/{loanId}/return")
    public LoanResponse returnBook(@PathVariable UUID loanId,
                                   @AuthenticationPrincipal Jwt jwt) {
        return service.returnBook(loanId, jwt);
    }
}
