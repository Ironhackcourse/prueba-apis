package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de petición para el endpoint de login.
 * Contiene las credenciales que el cliente envía: username y password.
 */
public record LoginRequest(

    @NotBlank(message = "El username es obligatorio")
    String username,

    @NotBlank(message = "El password es obligatorio")
    String password
) {}
