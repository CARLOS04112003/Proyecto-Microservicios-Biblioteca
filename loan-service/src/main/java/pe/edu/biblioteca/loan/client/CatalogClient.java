package pe.edu.biblioteca.loan.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pe.edu.biblioteca.common.exception.BadRequestException;
import pe.edu.biblioteca.common.exception.ConflictException;
import pe.edu.biblioteca.common.exception.NotFoundException;

import java.util.UUID;

@Component
public class CatalogClient {
    private final RestClient client;
    private final String internalApiKey;

    public CatalogClient(RestClient.Builder builder,
                         @Value("${services.catalog.base-url}") String baseUrl,
                         @Value("${app.internal-api-key}") String internalApiKey) {
        this.client = builder.baseUrl(baseUrl).build();
        this.internalApiKey = internalApiKey;
    }

    public CatalogBookResponse checkout(UUID bookId) {
        return post(bookId, "checkout");
    }

    public CatalogBookResponse checkin(UUID bookId) {
        return post(bookId, "checkin");
    }

    private CatalogBookResponse post(UUID bookId, String action) {
        return client.post()
                .uri("/internal/books/{id}/{action}", bookId, action)
                .header("X-Internal-Api-Key", internalApiKey)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        (request, response) -> { throw new NotFoundException("Libro no encontrado en el catálogo"); })
                .onStatus(status -> status.value() == 409,
                        (request, response) -> { throw new ConflictException("El catálogo rechazó la operación: disponibilidad inválida"); })
                .onStatus(HttpStatusCode::isError,
                        (request, response) -> { throw new BadRequestException("No se pudo completar la operación con el catálogo"); })
                .body(CatalogBookResponse.class);
    }
}
