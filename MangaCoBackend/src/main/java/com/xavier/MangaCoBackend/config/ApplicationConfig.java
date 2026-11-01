package com.xavier.MangaCoBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.xavier.MangaCoBackend.services.impl.UserDetailsServiceImpl;

/**
 * Configuración de beans de seguridad.
 *
 * Define los componentes que Spring Security necesita para funcionar:
 *
 *   1. PasswordEncoder (BCrypt): codifica y verifica contraseñas.
 *      - encode("admin123") → genera un hash como $2a$10$...
 *      - matches("admin123", hash) → compara texto plano con hash
 *
 *   2. UserDetailsService: carga usuarios desde la BD (UserDetailsServiceImpl).
 *
 *   3. DaoAuthenticationProvider: combina UserDetailsService + PasswordEncoder
 *      para autenticar usuarios (comprueba que el password enviado coincide con el hash de BD).
 *
 *   4. AuthenticationManager: punto de entrada que AuthenticationManager.authenticate()
 *      usa en AuthController para validar el login.
 */
@Configuration
public class ApplicationConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public ApplicationConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Codificador de contraseñas usando BCrypt.
     * Spring Security lo usa automáticamente para comparar passwords en el login.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación.
     * Lee el usuario de la BD (UserDetailsService) y verifica el password con BCrypt.
     * Se construye pasándole el UserDetailsService y luego se le asigna el PasswordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * AuthenticationManager expuesto como bean.
     * Se inyecta en AuthController para llamar a authenticate() en el login.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }
}
