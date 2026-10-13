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

@RestController
@RequestMapping("/api/v1/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping
    public ResponseEntity<List<SeriesResponse>> findAll(
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) SeriesStatus status,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(seriesService.findAll(enabled, status, categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeriesResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(seriesService.findById(id, includeDisabled));
    }

    @PostMapping
    public ResponseEntity<SeriesResponse> create(@Valid @RequestBody SeriesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seriesService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeriesResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SeriesRequest request) {
        return ResponseEntity.ok(seriesService.update(id, request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<SeriesResponse> enable(@PathVariable Long id) {
        seriesService.enable(id);
        return ResponseEntity.ok(seriesService.findById(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seriesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}