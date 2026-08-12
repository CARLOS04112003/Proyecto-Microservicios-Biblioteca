package pe.edu.biblioteca.reservation.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pe.edu.biblioteca.common.exception.BadRequestException;
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

    public CatalogBookResponse getBook(UUID bookId) {
        return client.get()
                .uri("/internal/books/{id}", bookId)
                .header("X-Internal-Api-Key", internalApiKey)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        (request, response) -> { throw new NotFoundException("Libro no encontrado en el catálogo"); })
                .onStatus(HttpStatusCode::isError,
                        (request, response) -> { throw new BadRequestException("No se pudo consultar el catálogo"); })
                .body(CatalogBookResponse.class);
    }
}
