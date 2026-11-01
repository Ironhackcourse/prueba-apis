package com.xavier.MangaCoBackend.dto.response;

/**
 * DTO de respuesta para usuarios.
 * IMPORTANTE: No incluye el password (por seguridad, nunca se devuelve al cliente).
 */
public record UserResponse(
    Long id,
    String username,
    String email,
    String roleName,   // nombre del rol (ej. "USER", "ADMIN")
    Boolean enabled
) {}
