package pe.edu.biblioteca.auth.service;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.biblioteca.auth.api.LoginRequest;
import pe.edu.biblioteca.auth.api.RegisterRequest;
import pe.edu.biblioteca.auth.api.TokenResponse;
import pe.edu.biblioteca.auth.api.UserResponse;
import pe.edu.biblioteca.auth.domain.Role;
import pe.edu.biblioteca.auth.domain.UserAccount;
import pe.edu.biblioteca.auth.repository.UserRepository;
import pe.edu.biblioteca.common.exception.ConflictException;
import pe.edu.biblioteca.common.exception.ForbiddenException;
import pe.edu.biblioteca.common.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtTokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("El correo ya se encuentra registrado");
        }
        UserAccount user = new UserAccount(
                request.fullName().trim(),
                email,
                passwordEncoder.encode(request.password()),
                Role.MEMBER
        );
        return UserResponse.from(repository.save(user));
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        UserAccount user = repository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ForbiddenException("Credenciales incorrectas"));
        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ForbiddenException("Credenciales incorrectas");
        }
        return tokenService.createToken(user);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return repository.findById(id).map(UserResponse::from)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return repository.findAll(Sort.by("fullName")).stream().map(UserResponse::from).toList();
    }
}
