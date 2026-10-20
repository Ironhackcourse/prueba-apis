package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.ComicRequest;
import com.xavier.MangaCoBackend.dto.response.ComicResponse;
import com.xavier.MangaCoBackend.services.ComicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de cómics (ejemplares/tomos).
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/comics},
 * con consultas filtradas por serie, editorial, país, formato y título.
 */
@RestController
@RequestMapping("/api/v1/comics")
@RequiredArgsConstructor
public class ComicController {

    private final ComicService comicService;

    /**
     * Lista los cómics aplicando filtros opcionales combinables.
     *
     * @param enabled     filtro opcional por estado de habilitación
     * @param seriesId    filtro opcional por id de serie
     * @param publisherId filtro opcional por id de editorial
     * @param countryId   filtro opcional por id de país
     * @param formatId    filtro opcional por id de formato
     * @param title       filtro opcional por título (parcial)
     * @return 200 OK con la lista de {@link ComicResponse}
     */
    @GetMapping
    public ResponseEntity<List<ComicResponse>> findAll(
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Long seriesId,
            @RequestParam(required = false) Long publisherId,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Long formatId,
            @RequestParam(required = false) String title) {
        return ResponseEntity.ok(
                comicService.findAll(enabled, seriesId, publisherId, countryId, formatId, title));
    }

    /**
     * Lista los cómics pertenecientes a una serie concreta.
     *
     * @param seriesId identificador de la serie
     * @return 200 OK con la lista de {@link ComicResponse} de la serie; 404 si la serie no existe
     */
    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<ComicResponse>> findBySeries(@PathVariable Long seriesId) {
        return ResponseEntity.ok(comicService.findBySeries(seriesId));
    }

    /**
     * Obtiene un cómic por su identificador.
     *
     * @param id              identificador del cómic
     * @param includeDisabled si es {@code true} también recupera cómics deshabilitados
     * @return 200 OK con {@link ComicResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComicResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(comicService.findById(id, includeDisabled));
    }

    /**
     * Crea un nuevo cómic a partir de los datos recibidos.
     *
     * @param request datos del cómic a crear ({@link ComicRequest}, validado)
     * @return 201 CREATED con {@link ComicResponse} del cómic creado; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<ComicResponse> create(@Valid @RequestBody ComicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comicService.create(request));
    }

    /**
     * Actualiza completamente un cómic existente.
     *
     * @param id      identificador del cómic a actualizar
     * @param request nuevos datos del cómic ({@link ComicRequest}, validado)
     * @return 200 OK con {@link ComicResponse} actualizado; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComicResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ComicRequest request) {
        return ResponseEntity.ok(comicService.update(id, request));
    }

    /**
     * Reactiva (habilita) un cómic previamente deshabilitado.
     *
     * @param id identificador del cómic a habilitar
     * @return 200 OK con {@link ComicResponse} del cómic habilitado; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<ComicResponse> enable(@PathVariable Long id) {
        comicService.enable(id);
        return ResponseEntity.ok(comicService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) un cómic.
     *
     * @param id identificador del cómic a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}