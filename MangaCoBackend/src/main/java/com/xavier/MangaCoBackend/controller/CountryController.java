package com.xavier.MangaCoBackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xavier.MangaCoBackend.dto.request.CountryRequest;
import com.xavier.MangaCoBackend.dto.response.CountryResponse;
import com.xavier.MangaCoBackend.services.CountryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de países de origen (autores, editoriales, series).
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/countries},
 * permitiendo consultar, crear, actualizar, habilitar y eliminar países.
 */
@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    /**
     * Lista todos los países, con filtro opcional por estado de habilitación.
     *
     * @param enabled filtro opcional: {@code true} solo habilitados, {@code false} solo deshabilitados,
     *                omitido para obtener todos
     * @return 200 OK con la lista de {@link CountryResponse}
     */
    @GetMapping
    public ResponseEntity<List<CountryResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(countryService.findAll(enabled));
    }

    /**
     * Obtiene un país por su identificador.
     *
     * @param id              identificador del país
     * @param includeDisabled si es {@code true} también recupera países deshabilitados
     * @return 200 OK con {@link CountryResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(countryService.findById(id, includeDisabled));
    }

    /**
     * Crea un nuevo país a partir de los datos recibidos.
     *
     * @param request datos del país a crear ({@link CountryRequest}, validado)
     * @return 201 CREATED con {@link CountryResponse} del país creado; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<CountryResponse> create(@Valid @RequestBody CountryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.create(request));
    }

    /**
     * Actualiza completamente un país existente.
     *
     * @param id      identificador del país a actualizar
     * @param request nuevos datos del país ({@link CountryRequest}, validado)
     * @return 200 OK con {@link CountryResponse} actualizado; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CountryRequest request) {
        return ResponseEntity.ok(countryService.update(id, request));
    }

    /**
     * Reactiva (habilita) un país previamente deshabilitado.
     *
     * @param id identificador del país a habilitar
     * @return 200 OK con {@link CountryResponse} del país habilitado; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<CountryResponse> enable(@PathVariable Long id) {
        countryService.enable(id);
        return ResponseEntity.ok(countryService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) un país.
     *
     * @param id identificador del país a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
