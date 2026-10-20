package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.SeriesRequest;
import com.xavier.MangaCoBackend.dto.response.SeriesResponse;
import com.xavier.MangaCoBackend.enums.SeriesStatus;
import com.xavier.MangaCoBackend.services.SeriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de series de manga/comic.
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/series},
 * con listados filtrables por estado de publicación y categoría.
 */
@RestController
@RequestMapping("/api/v1/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    /**
     * Lista todas las series, con filtros opcionales por estado de habilitación, estado de la serie y categoría.
     *
     * @param enabled    filtro opcional por estado de habilitación
     * @param status     filtro opcional por estado de la serie ({@link SeriesStatus})
     * @param categoryId filtro opcional por id de categoría
     * @return 200 OK con la lista de {@link SeriesResponse}
     */
    @GetMapping
    public ResponseEntity<List<SeriesResponse>> findAll(
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) SeriesStatus status,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(seriesService.findAll(enabled, status, categoryId));
    }

    /**
     * Obtiene una serie por su identificador.
     *
     * @param id              identificador de la serie
     * @param includeDisabled si es {@code true} también recupera series deshabilitadas
     * @return 200 OK con {@link SeriesResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<SeriesResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(seriesService.findById(id, includeDisabled));
    }

    /**
     * Crea una nueva serie a partir de los datos recibidos.
     *
     * @param request datos de la serie a crear ({@link SeriesRequest}, validado)
     * @return 201 CREATED con {@link SeriesResponse} de la serie creada; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<SeriesResponse> create(@Valid @RequestBody SeriesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seriesService.create(request));
    }

    /**
     * Actualiza completamente una serie existente.
     *
     * @param id      identificador de la serie a actualizar
     * @param request nuevos datos de la serie ({@link SeriesRequest}, validado)
     * @return 200 OK con {@link SeriesResponse} actualizada; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeriesResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SeriesRequest request) {
        return ResponseEntity.ok(seriesService.update(id, request));
    }

    /**
     * Reactiva (habilita) una serie previamente deshabilitada.
     *
     * @param id identificador de la serie a habilitar
     * @return 200 OK con {@link SeriesResponse} de la serie habilitada; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<SeriesResponse> enable(@PathVariable Long id) {
        seriesService.enable(id);
        return ResponseEntity.ok(seriesService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) una serie.
     *
     * @param id identificador de la serie a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seriesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}