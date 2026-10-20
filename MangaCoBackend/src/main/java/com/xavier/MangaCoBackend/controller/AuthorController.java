package com.xavier.MangaCoBackend.controller;

import com.xavier.MangaCoBackend.dto.request.AuthorRequest;
import com.xavier.MangaCoBackend.dto.response.AuthorResponse;
import com.xavier.MangaCoBackend.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de autores de cómics.
 * Expone los endpoints CRUD bajo la ruta base {@code /api/v1/authors},
 * permitiendo consultar (globalmente o por país), crear, actualizar, habilitar y eliminar autores.
 */
@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    /**
     * Lista todos los autores, con filtro opcional por estado de habilitación.
     *
     * @param enabled filtro opcional: {@code true} solo habilitados, {@code false} solo deshabilitados,
     *                omitido para obtener todos
     * @return 200 OK con la lista de {@link AuthorResponse}
     */
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(authorService.findAll(enabled));
    }

    /**
     * Lista los autores pertenecientes a un país concreto.
     *
     * @param countryId identificador del país
     * @return 200 OK con la lista de {@link AuthorResponse} del país; 404 si el país no existe
     */
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<AuthorResponse>> findByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(authorService.findByCountry(countryId));
    }

    /**
     * Obtiene un autor por su identificador.
     *
     * @param id              identificador del autor
     * @param includeDisabled si es {@code true} también recupera autores deshabilitados
     * @return 200 OK con {@link AuthorResponse}; 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(authorService.findById(id, includeDisabled));
    }

    /**
     * Crea un nuevo autor a partir de los datos recibidos.
     *
     * @param request datos del autor a crear ({@link AuthorRequest}, validado)
     * @return 201 CREATED con {@link AuthorResponse} del autor creado; 400 si la validación falla
     */
    @PostMapping
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(request));
    }

    /**
     * Actualiza completamente un autor existente.
     *
     * @param id      identificador del autor a actualizar
     * @param request nuevos datos del autor ({@link AuthorRequest}, validado)
     * @return 200 OK con {@link AuthorResponse} actualizado; 400 si la validación falla, 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.update(id, request));
    }

    /**
     * Reactiva (habilita) un autor previamente deshabilitado.
     *
     * @param id identificador del autor a habilitar
     * @return 200 OK con {@link AuthorResponse} del autor habilitado; 404 si no existe
     */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<AuthorResponse> enable(@PathVariable Long id) {
        authorService.enable(id);
        return ResponseEntity.ok(authorService.findById(id, true));
    }

    /**
     * Elimina (o deshabilita lógicamente) un autor.
     *
     * @param id identificador del autor a eliminar
     * @return 204 NO CONTENT sin cuerpo; 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}