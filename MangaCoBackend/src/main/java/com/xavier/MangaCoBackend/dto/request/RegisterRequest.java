package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de petición para registro de usuarios.
 *
 * Campos:
 *   - username: nombre de usuario (obligatorio, 3-50 caracteres)
 *   - email: correo electrónico (obligatorio, formato válido)
 *   - password: contraseña (obligatorio, mínimo 6 caracteres)
 *   - roleId: ID del rol (opcional). Si es null, se usa el rol por defecto USER (id=2).
 *     Solo el ADMIN puede enviar este campo; en el registro público se ignora y se fuerza USER.
 */
public record RegisterRequest(

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    String username,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    String email,

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 6, message = "El password debe tener al menos 6 caracteres")
    String password,

    Long roleId  // null permitido; el servicio decidirá el rol por defecto si no se envía
) {}
