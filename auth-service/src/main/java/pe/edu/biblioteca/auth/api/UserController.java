package pe.edu.biblioteca.auth.api;

import org.springframework.web.bind.annotation.*;
import pe.edu.biblioteca.auth.service.AuthService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return authService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable UUID id) {
        return authService.findById(id);
    }
}
