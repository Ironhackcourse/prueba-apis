package com.xavier.MangaCoBackend.dto.response;

public record RoleResponse(
    Long idRole,
    String nameRole,
    String description,
    Boolean enabled
) {}