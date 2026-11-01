package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.LoginRequest;
import com.xavier.MangaCoBackend.dto.request.RegisterRequest;
import com.xavier.MangaCoBackend.dto.response.AuthResponse;
import com.xavier.MangaCoBackend.dto.response.UserResponse;
import com.xavier.MangaCoBackend.config.JwtService;
import com.xavier.MangaCoBackend.services.UserService;
import com.xavier.MangaCoBackend.services.impl.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticación y registro de usuarios.
 *
 * Endpoints:
 *   POST /api/v1/auth/login       → Público. Autentica usuario y devuelve JWT.
 *   POST /api/v1/auth/register    → Público. Registra un nuevo usuario con rol USER (id=2).
 *
 * Ambos endpoints están bajo /api/v1/auth/** por lo que son públicos (no requieren token previo).
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService,
            UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Endpoint de login.
     * Autentica al usuario y devuelve un JWT válido por 24 horas.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // 1. Crear un token de autenticación sin verificar (username + password)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );

        // 2. AuthenticationManager valida las credenciales usando UserDetailsServiceImpl + PasswordEncoder
        authenticationManager.authenticate(authToken);

        // 3. Cargar el usuario real de la BD para obtener sus autoridades (roles)
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        // 4. Generar el JWT con los datos reales del usuario (incluye sus roles)
        String token = jwtService.generateToken(userDetails);

        // 5. Devolver token y username al cliente
        return ResponseEntity.ok(new AuthResponse(token, request.username()));
    }

    /**
     * Endpoint de registro público.
     * Cualquier persona puede registrarse. El rol se fuerza a USER (id=2) internamente en el servicio.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse created = userService.registerPublicUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
