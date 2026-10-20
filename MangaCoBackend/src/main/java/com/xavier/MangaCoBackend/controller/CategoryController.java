package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.CategoryRequest;
import com.xavier.MangaCoBackend.dto.response.CategoryResponse;
import com.xavier.MangaCoBackend.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de categorías de series y cómics.
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/categories},
 * permitiendo consultar, crear, actualizar, habilitar y eliminar categorías.
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Lista todas las categorías, con filtro opcional por estado de habilitación.
     *
     * @param enabled filtro opcional: {@code true} solo habilitadas, {@code false} solo deshabilitadas,
     *                omitido para obtener todas
     * @return 200 OK con la lista de {@link CategoryResponse}
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(categoryService.findAll(enabled));
    }

    /**
     * Obtiene una categoría por su identificador.
     *
     * @param id              identificador de la categoría
     * @param includeDisabled si es {@code true} también recupera categorías deshabilitadas
     * @return 200 OK con {@link CategoryResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(categoryService.findById(id, includeDisabled));
    }

    /**
     * Crea una nueva categoría a partir de los datos recibidos.
     *
     * @param request datos de la categoría a crear ({@link CategoryRequest}, validado)
     * @return 201 CREATED con {@link CategoryResponse} de la categoría creada; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }

    /**
     * Actualiza completamente una categoría existente.
     *
     * @param id      identificador de la categoría a actualizar
     * @param request nuevos datos de la categoría ({@link CategoryRequest}, validado)
     * @return 200 OK con {@link CategoryResponse} actualizada; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    /**
     * Reactiva (habilita) una categoría previamente deshabilitada.
     *
     * @param id identificador de la categoría a habilitar
     * @return 200 OK con {@link CategoryResponse} de la categoría habilitada; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<CategoryResponse> enable(@PathVariable Long id) {
        categoryService.enable(id);
        return ResponseEntity.ok(categoryService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) una categoría.
     *
     * @param id identificador de la categoría a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}