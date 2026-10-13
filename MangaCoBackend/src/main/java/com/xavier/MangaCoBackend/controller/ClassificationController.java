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

@RestController
@RequestMapping("/api/v1/classifications")
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    @GetMapping
    public ResponseEntity<List<ClassificationResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(classificationService.findAll(enabled));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassificationResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(classificationService.findById(id, includeDisabled));
    }

    @PostMapping
    public ResponseEntity<ClassificationResponse> create(@Valid @RequestBody ClassificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classificationService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassificationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ClassificationRequest request) {
        return ResponseEntity.ok(classificationService.update(id, request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ClassificationResponse> enable(@PathVariable Long id) {
        classificationService.enable(id);
        return ResponseEntity.ok(classificationService.findById(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}