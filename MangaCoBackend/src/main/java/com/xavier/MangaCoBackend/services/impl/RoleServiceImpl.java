package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.RoleRequest;
import com.xavier.MangaCoBackend.dto.response.RoleResponse;
import com.xavier.MangaCoBackend.entity.Role;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.RoleMapper;
import com.xavier.MangaCoBackend.repository.RoleRepository;
import com.xavier.MangaCoBackend.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> findAll(Boolean enabled) {
        List<Role> roles;
        if (enabled == null || enabled) {
            roles = roleRepository.findAllByEnabledTrue();
        } else {
            roles = roleRepository.findAllByEnabledFalse();
        }
        return roles.stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse findById(Long id, boolean includeDisabled) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role no encontrado con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(role.getEnabled())) {
            throw new ResourceNotFoundException("Role no encontrado con id: " + id);
        }
        return roleMapper.toResponse(role);
    }

    @Override
    @Transactional
    public RoleResponse create(RoleRequest request) {
        roleRepository.findByNameRoleIgnoreCase(request.nameRole())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe un rol activo con el nombre: " + request.nameRole());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe un rol deshabilitado con el nombre: '" + request.nameRole() +
                                "'. Reactívelo con PATCH /api/v1/roles/" + existing.getId() + "/enable");
                    }
                });

        Role role = roleMapper.toEntity(request);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleResponse update(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role no encontrado con id: " + id));
        roleMapper.updateEntity(role, request);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role no encontrado con id: " + id));
        if (Boolean.TRUE.equals(role.getEnabled())) {
            throw new DuplicateResourceException("El rol ya está habilitado");
        }
        role.setEnabled(true);
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role no encontrado con id: " + id));
        role.setEnabled(false);
        roleRepository.save(role);
    }
}