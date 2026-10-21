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

@RestController
@RequestMapping("/api/v1/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(publisherService.findAll(enabled));
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<PublisherResponse>> findByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(publisherService.findByCountry(countryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(publisherService.findById(id, includeDisabled));
    }

    @PostMapping
    public ResponseEntity<PublisherResponse> create(@Valid @RequestBody PublisherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PublisherRequest request) {
        return ResponseEntity.ok(publisherService.update(id, request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<PublisherResponse> enable(@PathVariable Long id) {
        publisherService.enable(id);
        return ResponseEntity.ok(publisherService.findById(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}