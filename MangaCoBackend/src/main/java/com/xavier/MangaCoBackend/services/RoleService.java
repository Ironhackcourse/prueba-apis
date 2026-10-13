package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.RoleRequest;
import com.xavier.MangaCoBackend.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    List<RoleResponse> findAll(Boolean enabled);

    RoleResponse findById(Long id, boolean includeDisabled);

    RoleResponse create(RoleRequest request);

    RoleResponse update(Long id, RoleRequest request);

    void enable(Long id);

    void delete(Long id);
}