package pe.edu.biblioteca.catalog.api;

import org.springframework.web.bind.annotation.*;
import pe.edu.biblioteca.catalog.service.BookService;
import pe.edu.biblioteca.catalog.service.InternalApiKeyValidator;

import java.util.UUID;

@RestController
@RequestMapping("/internal/books")
public class InternalBookController {
    private final BookService service;
    private final InternalApiKeyValidator keyValidator;

    public InternalBookController(BookService service, InternalApiKeyValidator keyValidator) {
        this.service = service;
        this.keyValidator = keyValidator;
    }

    @GetMapping("/{id}")
    public BookAvailabilityResponse get(@PathVariable UUID id,
                                        @RequestHeader(value = "X-Internal-Api-Key", required = false) String apiKey) {
        keyValidator.validate(apiKey);
        return service.availability(id);
    }

    @PostMapping("/{id}/checkout")
    public BookAvailabilityResponse checkout(@PathVariable UUID id,
                                             @RequestHeader(value = "X-Internal-Api-Key", required = false) String apiKey) {
        keyValidator.validate(apiKey);
        return service.checkout(id);
    }

    @PostMapping("/{id}/checkin")
    public BookAvailabilityResponse checkin(@PathVariable UUID id,
                                            @RequestHeader(value = "X-Internal-Api-Key", required = false) String apiKey) {
        keyValidator.validate(apiKey);
        return service.checkin(id);
    }
}
