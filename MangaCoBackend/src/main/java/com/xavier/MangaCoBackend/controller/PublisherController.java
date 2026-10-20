package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.PublisherRequest;
import com.xavier.MangaCoBackend.dto.response.PublisherResponse;
import com.xavier.MangaCoBackend.services.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de editoriales de cómics.
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/publishers},
 * permitiendo consultar (globalmente o por país), crear, actualizar, habilitar y eliminar editoriales.
 */
@RestController
@RequestMapping("/api/v1/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    /**
     * Lista todas las editoriales, con filtro opcional por estado de habilitación.
     *
     * @param enabled filtro opcional: {@code true} solo habilitadas, {@code false} solo deshabilitadas,
     *                omitido para obtener todas
     * @return 200 OK con la lista de {@link PublisherResponse}
     */
    @GetMapping
    public ResponseEntity<List<PublisherResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(publisherService.findAll(enabled));
    }

    /**
     * Lista las editoriales pertenecientes a un país concreto.
     *
     * @param countryId identificador del país
     * @return 200 OK con la lista de {@link PublisherResponse} del país; 404 si el país no existe
     */
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<PublisherResponse>> findByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(publisherService.findByCountry(countryId));
    }

    /**
     * Obtiene una editorial por su identificador.
     *
     * @param id              identificador de la editorial
     * @param includeDisabled si es {@code true} también recupera editoriales deshabilitadas
     * @return 200 OK con {@link PublisherResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(publisherService.findById(id, includeDisabled));
    }

    /**
     * Crea una nueva editorial a partir de los datos recibidos.
     *
     * @param request datos de la editorial a crear ({@link PublisherRequest}, validado)
     * @return 201 CREATED con {@link PublisherResponse} de la editorial creada; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<PublisherResponse> create(@Valid @RequestBody PublisherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.create(request));
    }

    /**
     * Actualiza completamente una editorial existente.
     *
     * @param id      identificador de la editorial a actualizar
     * @param request nuevos datos de la editorial ({@link PublisherRequest}, validado)
     * @return 200 OK con {@link PublisherResponse} actualizada; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PublisherRequest request) {
        return ResponseEntity.ok(publisherService.update(id, request));
    }

    /**
     * Reactiva (habilita) una editorial previamente deshabilitada.
     *
     * @param id identificador de la editorial a habilitar
     * @return 200 OK con {@link PublisherResponse} de la editorial habilitada; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<PublisherResponse> enable(@PathVariable Long id) {
        publisherService.enable(id);
        return ResponseEntity.ok(publisherService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) una editorial.
     *
     * @param id identificador de la editorial a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}