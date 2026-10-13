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

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(authorService.findAll(enabled));
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<AuthorResponse>> findByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(authorService.findByCountry(countryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(authorService.findById(id, includeDisabled));
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.update(id, request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<AuthorResponse> enable(@PathVariable Long id) {
        authorService.enable(id);
        return ResponseEntity.ok(authorService.findById(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}