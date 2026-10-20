package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.ClassificationRequest;
import com.xavier.MangaCoBackend.dto.response.ClassificationResponse;
import com.xavier.MangaCoBackend.services.ClassificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de clasificaciones (ratings/edades) de los cómics.
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/classifications},
 * permitiendo consultar, crear, actualizar, habilitar y eliminar clasificaciones.
 */
@RestController
@RequestMapping("/api/v1/classifications")
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    /**
     * Lista todas las clasificaciones, con filtro opcional por estado de habilitación.
     *
     * @param enabled filtro opcional: {@code true} solo habilitadas, {@code false} solo deshabilitadas,
     *                omitido para obtener todas
     * @return 200 OK con la lista de {@link ClassificationResponse}
     */
    @GetMapping
    public ResponseEntity<List<ClassificationResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(classificationService.findAll(enabled));
    }

    /**
     * Obtiene una clasificación por su identificador.
     *
     * @param id              identificador de la clasificación
     * @param includeDisabled si es {@code true} también recupera clasificaciones deshabilitadas
     * @return 200 OK con {@link ClassificationResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassificationResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(classificationService.findById(id, includeDisabled));
    }

    /**
     * Crea una nueva clasificación a partir de los datos recibidos.
     *
     * @param request datos de la clasificación a crear ({@link ClassificationRequest}, validado)
     * @return 201 CREATED con {@link ClassificationResponse} de la clasificación creada; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<ClassificationResponse> create(@Valid @RequestBody ClassificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classificationService.create(request));
    }

    /**
     * Actualiza completamente una clasificación existente.
     *
     * @param id      identificador de la clasificación a actualizar
     * @param request nuevos datos de la clasificación ({@link ClassificationRequest}, validado)
     * @return 200 OK con {@link ClassificationResponse} actualizada; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassificationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ClassificationRequest request) {
        return ResponseEntity.ok(classificationService.update(id, request));
    }

    /**
     * Reactiva (habilita) una clasificación previamente deshabilitada.
     *
     * @param id identificador de la clasificación a habilitar
     * @return 200 OK con {@link ClassificationResponse} de la clasificación habilitada; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<ClassificationResponse> enable(@PathVariable Long id) {
        classificationService.enable(id);
        return ResponseEntity.ok(classificationService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) una clasificación.
     *
     * @param id identificador de la clasificación a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}