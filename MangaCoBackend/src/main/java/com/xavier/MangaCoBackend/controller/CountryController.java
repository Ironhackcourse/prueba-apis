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

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryResponse>> findAll(
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(countryService.findAll(enabled));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> findById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeDisabled) {
        return ResponseEntity.ok(countryService.findById(id, includeDisabled));
    }

    @PostMapping
    public ResponseEntity<CountryResponse> create(@Valid @RequestBody CountryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CountryRequest request) {
        return ResponseEntity.ok(countryService.update(id, request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<CountryResponse> enable(@PathVariable Long id) {
        countryService.enable(id);
        return ResponseEntity.ok(countryService.findById(id, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
