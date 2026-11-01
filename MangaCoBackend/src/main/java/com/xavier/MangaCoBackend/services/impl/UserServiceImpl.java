package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.RegisterRequest;
import com.xavier.MangaCoBackend.dto.response.UserResponse;
import com.xavier.MangaCoBackend.entity.Role;
import com.xavier.MangaCoBackend.entity.User;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.repository.RoleRepository;
import com.xavier.MangaCoBackend.repository.UserRepository;
import com.xavier.MangaCoBackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de usuarios.
 *
 * Flujo de negocio:
 *   1. registerPublicUser(): cualquiera se registra como USER (rol forzado a id=2).
 *   2. createUserByAdmin(): solo ADMIN puede crear usuarios y elegir su rol.
 *   3. updateUser(): solo ADMIN puede actualizar username/email/password. NUNCA el rol.
 *   4. deleteUser(): solo ADMIN. Soft delete (enabled=false).
 *   5. findAllUsers(): solo ADMIN. Lista usuarios sin exponer passwords.
 *
 * Todas las operaciones validan que username y email no estén duplicados antes de guardar.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ID del rol USER por defecto (según las reglas de negocio)
    private static final long DEFAULT_USER_ROLE_ID = 2L;

    /* ------------------------------------------------------------------ */
    /*  REGISTRO PÚBLICO (cualquier persona)                              */
    /* ------------------------------------------------------------------ */

    @Override
    public UserResponse registerPublicUser(RegisterRequest request) {
        // Validar duplicados antes de crear
        validateUsernameAndEmailUnique(request.username(), request.email());

        // Buscar el rol USER (id=2) en la base de datos
        Role userRole = roleRepository.findById(DEFAULT_USER_ROLE_ID)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol USER (id=2) no encontrado en la base de datos"));

        // Crear la entidad User
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // encriptar contraseña
                .role(userRole)                                       // rol forzado a USER
                .enabled(true)
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    /* ------------------------------------------------------------------ */
    /*  CREACIÓN POR ADMIN                                                */
    /* ------------------------------------------------------------------ */

    @Override
    public UserResponse createUserByAdmin(RegisterRequest request) {
        // Validar duplicados
        validateUsernameAndEmailUnique(request.username(), request.email());

        // Determinar el rol: si el admin envió roleId, usarlo; si no, USER (id=2)
        Long roleId = (request.roleId() != null) ? request.roleId() : DEFAULT_USER_ROLE_ID;
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol con id=" + roleId + " no encontrado"));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .enabled(true)
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    /* ------------------------------------------------------------------ */
    /*  ACTUALIZACIÓN (solo ADMIN)                                        */
    /* ------------------------------------------------------------------ */

    @Override
    public UserResponse updateUser(Long id, RegisterRequest request) {
        // Buscar el usuario existente
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));

        // Validar que el nuevo username/email no pertenezcan a OTRO usuario
        validateUsernameAndEmailUniqueForUpdate(id, request.username(), request.email());

        // Actualizar solo los campos permitidos: username, email, password
        // IMPORTANTE: NUNCA se actualiza el rol aquí
        user.setUsername(request.username());
        user.setEmail(request.email());

        // Si se envía password nueva, la encriptamos; si no, se deja la actual
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    /* ------------------------------------------------------------------ */
    /*  ELIMINACIÓN LÓGICA (soft delete)                                  */
    /* ------------------------------------------------------------------ */

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));

        // Soft delete: no borramos la fila, solo deshabilitamos
        user.setEnabled(false);
        userRepository.save(user);
    }

    /* ------------------------------------------------------------------ */
    /*  LISTADO                                                           */
    /* ------------------------------------------------------------------ */

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* ------------------------------------------------------------------ */
    /*  CAMBIO DE ROL (solo ADMIN)                                        */
    /* ------------------------------------------------------------------ */

    @Override
    public UserResponse changeUserRole(Long id, Long roleId) {
        // 1. Buscar el usuario cuyo rol queremos cambiar
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));

        // 2. Buscar el nuevo rol en la base de datos
        Role newRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol con id=" + roleId + " no encontrado en la base de datos"));

        // 3. Asignar el nuevo rol y guardar
        user.setRole(newRole);
        User saved = userRepository.save(user);

        return mapToResponse(saved);
    }

    /* ------------------------------------------------------------------ */
    /*  PERFIL PROPIO (cualquier usuario autenticado)                     */
    /* ------------------------------------------------------------------ */

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserProfile() {
        // 1. Obtener la autenticación actual del contexto de seguridad de Spring
        //    Esto viene del JWT validado por el JwtAuthenticationFilter
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No hay un usuario autenticado");
        }

        // 2. El nombre de usuario (principal) está en el token JWT
        String username = authentication.getName();

        // 3. Buscar el usuario en la base de datos por ese username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con username: " + username));

        return mapToResponse(user);
    }

    /* ------------------------------------------------------------------ */
    /*  MÉTODOS PRIVADOS AUXILIARES                                       */
    /* ------------------------------------------------------------------ */

    /**
     * Valida que username y email no existan en la base de datos.
     * Si ya existen, lanza DuplicateResourceException.
     */
    private void validateUsernameAndEmailUnique(String username, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el username: " + username);
        }
       /*  if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el email: " + email);
        }
                    */
    }

    /**
     * Valida duplicados para actualización, excluyendo el usuario que se está editando.
     * Esto permite que un usuario mantenga su propio username/email sin que se considere duplicado.
     */
    private void validateUsernameAndEmailUniqueForUpdate(Long id, String username, String email) {
        userRepository.findByUsername(username).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException(
                        "Ya existe un usuario con el username: " + username);
            }
        });
       /* userRepository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException(
                        "Ya existe un usuario con el email: " + email);
            }
        });
         */
    }

    /**
     * Convierte una entidad User a UserResponse (sin exponer el password).
     */
    private UserResponse mapToResponse(User user) {
        String roleName = (user.getRole() != null) ? user.getRole().getNameRole() : "UNKNOWN";
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleName,
                user.getEnabled()
        );
    }
}
