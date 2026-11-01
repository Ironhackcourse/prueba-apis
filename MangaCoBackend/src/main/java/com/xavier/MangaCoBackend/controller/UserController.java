package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.ChangeRoleRequest;
import com.xavier.MangaCoBackend.dto.request.RegisterRequest;
import com.xavier.MangaCoBackend.dto.response.UserResponse;
import com.xavier.MangaCoBackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de gestión de usuarios.
 *
 * Reglas de acceso:
 *   - POST /api/v1/users              → Solo ADMIN (crear usuario con rol).
 *   - GET  /api/v1/users              → Solo ADMIN (listar todos los usuarios).
 *   - PUT  /api/v1/users/{id}         → ADMIN puede editar cualquier usuario;
 *                                        USER puede editar solo su propio perfil (#id == principal.id).
 *                                        Nunca se cambia el rol aquí.
 *   - DELETE /api/v1/users/{id}       → Solo ADMIN (soft delete).
 *   - PUT /api/v1/users/{id}/role     → Solo ADMIN (cambiar rol de cualquier usuario).
 *   - GET  /api/v1/users/me           → Cualquier usuario autenticado (ver propio perfil).
 *
 * Nota: el registro público de usuarios está en AuthController (/api/v1/auth/register).
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creación de usuario por ADMIN.
     * El admin puede especificar el roleId en el request (USER o ADMIN).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> createUserByAdmin(@RequestBody RegisterRequest request) {
        UserResponse created = userService.createUserByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Listado de todos los usuarios (sin passwords).
     */
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Actualización de usuario.
     * ADMIN puede editar cualquier usuario. USER solo puede editar su propio perfil.
     * Permite cambiar username, email y password. Nunca se cambia el rol.
     */
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @RequestBody RegisterRequest request) {
        UserResponse updated = userService.updateUser(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Eliminación lógica (soft delete) de usuario (solo ADMIN).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cambia el rol de un usuario (solo ADMIN).
     * El ADMIN puede asignar cualquier rol existente (USER, ADMIN, etc.).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @RequestBody ChangeRoleRequest request) {
        UserResponse updated = userService.changeUserRole(id, request.roleId());
        return ResponseEntity.ok(updated);
    }

    /**
     * Devuelve el perfil del usuario actualmente autenticado.
     * Cualquier usuario autenticado puede ver su propio perfil.
     * El username se obtiene del JWT a través del SecurityContext.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile() {
        UserResponse profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }
}
