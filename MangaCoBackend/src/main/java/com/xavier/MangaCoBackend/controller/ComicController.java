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

@RestController
@RequestMapping("/api/v1/comics")
@RequiredArgsConstructor
public class ComicController {

    private final ComicService comicService;

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

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<ComicResponse>> findBySeries(@PathVariable Long seriesId) {
        return ResponseEntity.ok(comicService.findBySeries(seriesId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComicResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(comicService.findById(id, includeDisabled));
    }

    @PostMapping
    public ResponseEntity<ComicResponse> create(@Valid @RequestBody ComicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comicService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComicResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ComicRequest request) {
        return ResponseEntity.ok(comicService.update(id, request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ComicResponse> enable(@PathVariable Long id) {
        comicService.enable(id);
        return ResponseEntity.ok(comicService.findById(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}