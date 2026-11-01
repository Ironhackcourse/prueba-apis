package com.xavier.MangaCoBackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de seguridad de Spring Security.
 *
 * Reglas de autorización definidas:
 *   - /api/v1/auth/**      → públicos (login, registro, etc.)
 *   - GET cualquier ruta   → públicos (lectura sin token, con o sin registro)
 *   - POST /api/v1/users   → solo ADMIN (crear usuario)
 *   - PUT /api/v1/users/{id} → autenticado (ADMIN o dueño del perfil, resuelto en @PreAuthorize)
 *   - DELETE /api/v1/users/{id} → solo ADMIN (soft delete)
 *   - /api/v1/users/**     → solo ADMIN (gestión de usuarios)
 *
 * Características importantes:
 *   - CSRF desactivado: usamos JWT stateless, no necesitamos protección CSRF (es
 *     para sesiones con cookies)
 *   - SessionCreationPolicy.STATELESS: Spring Security NO crea ni usa sesiones
 *     HTTP. Cada petición lleva su propio JWT en el header Authorization. El
 *     servidor no guarda estado de sesión entre peticiones, lo cual es ideal
 *     para APIs REST y para escalar horizontalmente.
 *   - Se añade JwtAuthenticationFilter ANTES de UsernamePasswordAuthenticationFilter
 *     para que el token JWT se procese antes de la autenticación estándar.
 *   - @EnableMethodSecurity activa el soporte de anotaciones @PreAuthorize en
 *     los controladores.
 *   - CORS se gestiona en CorsConfig.java (WebMvcConfigurer), no se duplica aquí.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationManager authenticationManager;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthFilter,
            AuthenticationManager authenticationManager) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Define la cadena de filtros de seguridad (SecurityFilterChain).
     * Aquí configuramos qué endpoints son públicos, cuáles requieren ADMIN,
     * desactivamos CSRF y establecemos sesión STATELESS.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF desactivado porque usamos JWT stateless (no cookies de sesión)
                .csrf(csrf -> csrf.disable())

                // No usamos sesiones HTTP; cada petición es autenticada por su propio JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Reglas de autorización por endpoint y método HTTP
                .authorizeHttpRequests(auth -> auth
                        // Endpoints de auth son públicos (login y registro)
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Ver propio perfil: requiere estar autenticado (cualquier rol)
                        .requestMatchers("/api/v1/users/me").authenticated()

                        // OPTIONS (preflight CORS) sin autenticación — manejado por CorsConfig
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Todos los GET son públicos (lectura sin token, con o sin registro)
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        // Crear usuario (POST /api/v1/users) solo ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")

                        // Demás POST solo ADMIN
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")

                        // Editar usuario propio o admin: se resuelve en @PreAuthorize del controller
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/{id}").authenticated()

                        // Demás PUT solo ADMIN
                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")

                        // PATCH solo ADMIN
                        .requestMatchers(HttpMethod.PATCH, "/**").hasRole("ADMIN")

                        // DELETE solo ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated())

                // Añademos nuestro filtro JWT antes del filtro de autenticación de
                // username/password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}