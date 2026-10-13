package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.RoleRequest;
import com.xavier.MangaCoBackend.dto.response.RoleResponse;
import com.xavier.MangaCoBackend.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequest request) {
        return Role.builder()
                .nameRole(request.nameRole())
                .description(request.description())
                .build();
    }

    public RoleResponse toResponse(Role entity) {
        return new RoleResponse(
                entity.getId(),
                entity.getNameRole(),
                entity.getDescription(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Role entity, RoleRequest request) {
        entity.setNameRole(request.nameRole());
        entity.setDescription(request.description());
    }
}