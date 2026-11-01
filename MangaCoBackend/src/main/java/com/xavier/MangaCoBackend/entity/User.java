package com.xavier.MangaCoBackend.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entidad User mapeada a la tabla `user` de la base de datos.
 *
 * Implementa {@link UserDetails} de Spring Security para que Spring Security
 * pueda autenticar y autorizar a este usuario directamente desde la BD.
 *
 * Métodos de UserDetails que implementamos:
 *   - getUsername()     → devuelve el username (campo de la entidad)
 *   - getPassword()     → devuelve el password hash (campo de la entidad)
 *   - getAuthorities()  → devuelve los roles/permisos (ej. "ROLE_ADMIN")
 *   - isAccountNonExpired()   → siempre true (no usamos expiración de cuenta)
 *   - isAccountNonLocked()    → siempre true (no usamos bloqueo de cuenta)
 *   - isCredentialsNonExpired() → siempre true (no usamos expiración de credenciales)
 *   - isEnabled()   → devuelve el campo enabled de la entidad
 */
@Entity
@Table(name = "user",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_username", columnNames = {"username"}),
        @UniqueConstraint(name = "uk_user_email", columnNames = {"email"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.enabled = (this.enabled == null) ? true : this.enabled;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // -----------------------------------------------------------------------
    // Implementación de UserDetails (Spring Security)
    // -----------------------------------------------------------------------

    /**
     * Devuelve los permisos del usuario.
     * Spring Security los usa para decidir si el usuario puede acceder a un endpoint.
     * Devolvemos un SimpleGrantedAuthority con el nombre del rol precedido de "ROLE_",
     * porque Spring Security espera ese prefijo para las anotaciones @PreAuthorize / hasRole.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getNameRole()));
    }

    /**
     * Devuelve el password almacenado en BD (ya hash con BCrypt).
     * Spring Security lo comparará con el password que el usuario envía en el login.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Devuelve el username. Spring Security lo usa como identificador principal del usuario.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indica si la cuenta ha expirado. Devolvemos true (no expirada).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta está bloqueada. Devolvemos true (no bloqueada).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales (password) han expirado. Devolvemos true (no expiradas).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado. Mapeamos al campo `enabled` de la entidad.
     */
    @Override
    public boolean isEnabled() {
        return enabled != null && enabled;
    }
}
