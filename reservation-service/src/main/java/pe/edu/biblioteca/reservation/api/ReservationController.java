package pe.edu.biblioteca.reservation.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pe.edu.biblioteca.reservation.service.ReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody CreateReservationRequest request,
                                      @AuthenticationPrincipal Jwt jwt) {
        return service.create(request, jwt);
    }

    @GetMapping("/me")
    public List<ReservationResponse> myReservations(@AuthenticationPrincipal Jwt jwt) {
        return service.myReservations(jwt);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public List<ReservationResponse> allReservations() {
        return service.allReservations();
    }

    @PostMapping("/{id}/cancel")
    public ReservationResponse cancel(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        return service.cancel(id, jwt);
    }
}
