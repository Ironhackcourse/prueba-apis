package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para cambiar el rol de un usuario.
 * Solo el ADMIN puede usar este endpoint.
 *
 * Campo:
 *   - roleId: ID del nuevo rol (1 = ADMIN, 2 = USER, etc.)
 */
public record ChangeRoleRequest(

    @NotNull(message = "El ID del rol es obligatorio")
    Long roleId
) {}
