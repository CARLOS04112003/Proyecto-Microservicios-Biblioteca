package pe.edu.biblioteca.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.biblioteca.catalog.api.BookRequest;
import pe.edu.biblioteca.catalog.service.BookService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceIntegrationTest {
    @Autowired
    BookService service;

    @Test
    void checkoutAndCheckinModifyAvailability() {
        var created = service.create(new BookRequest("TEST-001", "Libro de prueba", "Autor", "Pruebas", null, 1));
        var checkedOut = service.checkout(created.id());
        assertThat(checkedOut.availableCopies()).isZero();
        assertThatThrownBy(() -> service.checkout(created.id())).hasMessageContaining("disponibles");
        var checkedIn = service.checkin(created.id());
        assertThat(checkedIn.availableCopies()).isEqualTo(1);
    }
}
