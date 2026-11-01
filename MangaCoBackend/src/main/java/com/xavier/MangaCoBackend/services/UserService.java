package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.RegisterRequest;
import com.xavier.MangaCoBackend.dto.response.UserResponse;

import java.util.List;

/**
 * Interfaz del servicio de usuarios.
 * Define las operaciones de negocio para gestión de usuarios:
 * registro, creación, actualización, eliminación, listado, cambio de rol y perfil propio.
 */
public interface UserService {

    /**
     * Registro público: cualquier persona puede registrarse.
     * Se fuerza el rol USER (id=2) siempre, sin importar lo que envíe el cliente.
     */
    UserResponse registerPublicUser(RegisterRequest request);

    /**
     * Creación de usuario por ADMIN. El ADMIN puede elegir el rol.
     * Si roleId es null, se usa USER (id=2) por defecto.
     */
    UserResponse createUserByAdmin(RegisterRequest request);

    /**
     * Actualización de usuario (solo ADMIN).
     * Permite actualizar username, email y password. NUNCA se actualiza el rol.
     */
    UserResponse updateUser(Long id, RegisterRequest request);

    /**
     * Eliminación lógica (soft delete): pone enabled = false.
     */
    void deleteUser(Long id);

    /**
     * Listado de todos los usuarios (sin contraseñas).
     */
    List<UserResponse> findAllUsers();

    /**
     * Cambia el rol de un usuario (solo ADMIN).
     * No se puede cambiar el rol de un usuario a uno inexistente.
     */
    UserResponse changeUserRole(Long id, Long roleId);

    /**
     * Devuelve el perfil del usuario actualmente autenticado (obtenido del JWT/SecurityContext).
     * Cualquier usuario autenticado puede ver su propio perfil.
     */
    UserResponse getCurrentUserProfile();
}
