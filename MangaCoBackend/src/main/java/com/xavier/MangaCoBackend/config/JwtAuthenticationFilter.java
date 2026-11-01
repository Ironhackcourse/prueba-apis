package com.xavier.MangaCoBackend.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.xavier.MangaCoBackend.services.impl.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro JWT que se ejecuta en cada petición HTTP.
 *
 * Extiende OncePerRequestFilter para garantizar que el filtro se ejecute una
 * sola vez por petición.
 *
 * Flujo paso a paso:
 * 1. Leemos el header "Authorization" de la petición HTTP.
 * 2. Si el header empieza con "Bearer ", extraemos el token (quitamos el
 * prefijo "Bearer ").
 * 3. Si hay token y no hay autenticación previa en el SecurityContext:
 * a. Extraemos el username del token con JwtService.extractUsername()
 * b. Cargamos los detalles del usuario desde la BD con UserDetailsServiceImpl
 * c. Validamos el token con JwtService.isTokenValid(token, userDetails)
 * d. Si es válido, creamos un UsernamePasswordAuthenticationToken con los
 * authorities del usuario
 * e. Lo guardamos en SecurityContextHolder para que Spring Security sepa que el
 * usuario está autenticado
 * 4. Continuamos con la siguiente cadena de filtros (filterChain.doFilter)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Leer el header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Si no hay header o no empieza con "Bearer ", saltamos este filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (sin el prefijo "Bearer ")
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // 4. Si tenemos username y no hay autenticación previa en el contexto de
        // seguridad
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Cargar los detalles del usuario desde la BD
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Validar el token contra los detalles del usuario
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Crear el objeto de autenticación con los roles/authorities del usuario
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                // Establecer detalles de la petición (IP, etc.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                // Guardar la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 5. Continuar con el resto de filtros
        filterChain.doFilter(request, response);
    }
}
