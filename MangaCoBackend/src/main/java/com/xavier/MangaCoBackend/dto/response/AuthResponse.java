package com.xavier.MangaCoBackend.dto.response;

/**
 * DTO de respuesta para el endpoint de login.
 * Contiene el JWT generado y el username del usuario autenticado.
 */
public record AuthResponse(
    String token,
    String username
) {}
