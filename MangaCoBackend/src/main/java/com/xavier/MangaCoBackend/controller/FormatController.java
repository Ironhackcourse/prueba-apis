package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.FormatRequest;
import com.xavier.MangaCoBackend.dto.response.FormatResponse;
import com.xavier.MangaCoBackend.services.FormatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de formatos editoriales de los cómics (tomo, digital, omnibus, etc.).
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/formats},
 * permitiendo consultar, crear, actualizar, habilitar y eliminar formatos.
 */
@RestController
@RequestMapping("/api/v1/formats")
@RequiredArgsConstructor
public class FormatController {

    private final FormatService formatService;

    /**
     * Lista todos los formatos, con filtro opcional por estado de habilitación.
     *
     * @param enabled filtro opcional: {@code true} solo habilitados, {@code false} solo deshabilitados,
     *                omitido para obtener todos
     * @return 200 OK con la lista de {@link FormatResponse}
     */
    @GetMapping
    public ResponseEntity<List<FormatResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(formatService.findAll(enabled));
    }

    /**
     * Obtiene un formato por su identificador.
     *
     * @param id              identificador del formato
     * @param includeDisabled si es {@code true} también recupera formatos deshabilitados
     * @return 200 OK con {@link FormatResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormatResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(formatService.findById(id, includeDisabled));
    }

    /**
     * Crea un nuevo formato a partir de los datos recibidos.
     *
     * @param request datos del formato a crear ({@link FormatRequest}, validado)
     * @return 201 CREATED con {@link FormatResponse} del formato creado; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<FormatResponse> create(@Valid @RequestBody FormatRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(formatService.create(request));
    }

    /**
     * Actualiza completamente un formato existente.
     *
     * @param id      identificador del formato a actualizar
     * @param request nuevos datos del formato ({@link FormatRequest}, validado)
     * @return 200 OK con {@link FormatResponse} actualizado; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormatResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FormatRequest request) {
        return ResponseEntity.ok(formatService.update(id, request));
    }

    /**
     * Reactiva (habilita) un formato previamente deshabilitado.
     *
     * @param id identificador del formato a habilitar
     * @return 200 OK con {@link FormatResponse} del formato habilitado; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<FormatResponse> enable(@PathVariable Long id) {
        formatService.enable(id);
        return ResponseEntity.ok(formatService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) un formato.
     *
     * @param id identificador del formato a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}