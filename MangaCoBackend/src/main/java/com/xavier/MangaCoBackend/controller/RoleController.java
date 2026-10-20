package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.RoleRequest;
import com.xavier.MangaCoBackend.dto.response.RoleResponse;
import com.xavier.MangaCoBackend.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de roles de usuario.
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/roles},
 * permitiendo consultar, crear, actualizar, habilitar y eliminar roles.
 */
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Lista todos los roles, con filtro opcional por estado de habilitación.
     *
     * @param enabled filtro opcional: {@code true} solo habilitados, {@code false} solo deshabilitados,
     *                omitido para obtener todos
     * @return 200 OK con la lista de {@link RoleResponse}
     */
    @GetMapping
    public ResponseEntity<List<RoleResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(roleService.findAll(enabled));
    }

    /**
     * Obtiene un rol por su identificador.
     *
     * @param id              identificador del rol
     * @param includeDisabled si es {@code true} también recupera roles deshabilitados
     * @return 200 OK con {@link RoleResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(roleService.findById(id, includeDisabled));
    }

    /**
     * Crea un nuevo rol a partir de los datos recibidos.
     *
     * @param request datos del rol a crear ({@link RoleRequest}, validado)
     * @return 201 CREATED con {@link RoleResponse} del rol creado; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(request));
    }

    /**
     * Actualiza completamente un rol existente.
     *
     * @param id      identificador del rol a actualizar
     * @param request nuevos datos del rol ({@link RoleRequest}, validado)
     * @return 200 OK con {@link RoleResponse} actualizado; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.update(id, request));
    }

    /**
     * Reactiva (habilita) un rol previamente deshabilitado.
     *
     * @param id identificador del rol a habilitar
     * @return 200 OK con {@link RoleResponse} del rol habilitado; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<RoleResponse> enable(@PathVariable Long id) {
        roleService.enable(id);
        return ResponseEntity.ok(roleService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) un rol.
     *
     * @param id identificador del rol a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}