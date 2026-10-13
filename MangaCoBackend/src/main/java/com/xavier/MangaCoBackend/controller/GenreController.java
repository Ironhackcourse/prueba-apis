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

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(genreService.findAll(enabled));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(genreService.findById(id, includeDisabled));
    }

    @PostMapping
    public ResponseEntity<GenreResponse> create(@Valid @RequestBody GenreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody GenreRequest request) {
        return ResponseEntity.ok(genreService.update(id, request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<GenreResponse> enable(@PathVariable Long id) {
        genreService.enable(id);
        return ResponseEntity.ok(genreService.findById(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}