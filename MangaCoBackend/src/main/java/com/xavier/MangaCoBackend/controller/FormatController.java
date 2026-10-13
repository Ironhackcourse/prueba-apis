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

@RestController
@RequestMapping("/api/v1/formats")
@RequiredArgsConstructor
public class FormatController {

    private final FormatService formatService;

    @GetMapping
    public ResponseEntity<List<FormatResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(formatService.findAll(enabled));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormatResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(formatService.findById(id, includeDisabled));
    }

    @PostMapping
    public ResponseEntity<FormatResponse> create(@Valid @RequestBody FormatRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(formatService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormatResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody FormatRequest request) {
        return ResponseEntity.ok(formatService.update(id, request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<FormatResponse> enable(@PathVariable Long id) {
        formatService.enable(id);
        return ResponseEntity.ok(formatService.findById(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}