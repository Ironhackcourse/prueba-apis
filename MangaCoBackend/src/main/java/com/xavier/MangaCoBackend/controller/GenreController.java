package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.GenreRequest;
import com.xavier.MangaCoBackend.dto.response.GenreResponse;
import com.xavier.MangaCoBackend.services.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de géneros literarios/editoriales de los cómics.
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/genres},
 * permitiendo consultar, crear, actualizar, habilitar y eliminar géneros.
 */
@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    /**
     * Lista todos los géneros, con filtro opcional por estado de habilitación.
     *
     * @param enabled filtro opcional: {@code true} solo habilitados, {@code false} solo deshabilitados,
     *                omitido para obtener todos
     * @return 200 OK con la lista de {@link GenreResponse}
     */
    @GetMapping
    public ResponseEntity<List<GenreResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(genreService.findAll(enabled));
    }

    /**
     * Obtiene un género por su identificador.
     *
     * @param id              identificador del género
     * @param includeDisabled si es {@code true} también recupera géneros deshabilitados
     * @return 200 OK con {@link GenreResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(genreService.findById(id, includeDisabled));
    }

    /**
     * Crea un nuevo género a partir de los datos recibidos.
     *
     * @param request datos del género a crear ({@link GenreRequest}, validado)
     * @return 201 CREATED con {@link GenreResponse} del género creado; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<GenreResponse> create(@Valid @RequestBody GenreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(request));
    }

    /**
     * Actualiza completamente un género existente.
     *
     * @param id      identificador del género a actualizar
     * @param request nuevos datos del género ({@link GenreRequest}, validado)
     * @return 200 OK con {@link GenreResponse} actualizado; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody GenreRequest request) {
        return ResponseEntity.ok(genreService.update(id, request));
    }

    /**
     * Reactiva (habilita) un género previamente deshabilitado.
     *
     * @param id identificador del género a habilitar
     * @return 200 OK con {@link GenreResponse} del género habilitado; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<GenreResponse> enable(@PathVariable Long id) {
        genreService.enable(id);
        return ResponseEntity.ok(genreService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) un género.
     *
     * @param id identificador del género a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}