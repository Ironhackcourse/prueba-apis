package com.xavier.MangaCoBackend.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xavier.MangaCoBackend.dto.request.CountryRequest;
import com.xavier.MangaCoBackend.dto.response.CountryResponse;
import com.xavier.MangaCoBackend.entity.Country;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.CountryMapper;
import com.xavier.MangaCoBackend.repository.CountryRepository;
import com.xavier.MangaCoBackend.services.CountryService;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CountryResponse> findAll(Boolean enabled) {
        List<Country> countries;
        if (enabled == null || enabled) {
            countries = countryRepository.findAllByEnabledTrue();
        } else {
            countries = countryRepository.findAllByEnabledFalse();
        }
        return countries.stream()
                .map(countryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CountryResponse findById(Long id, boolean includeDisabled) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country no encontrado con id: " + id));

        if (!includeDisabled && Boolean.FALSE.equals(country.getEnabled())) {
            throw new ResourceNotFoundException("Country no encontrado con id: " + id);
        }
        return countryMapper.toResponse(country);
    }

    @Override
    @Transactional
    public CountryResponse create(CountryRequest request) {
        countryRepository.findByNameCountryIgnoreCase(request.nameCountry())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe un país activo con el nombre: " + request.nameCountry());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe un país deshabilitado con el nombre: '" + request.nameCountry() +
                                        "'. Reactívelo con PATCH /api/v1/countries/" + existing.getId()
                                        + "/enable");
                    }
                });

        Country country = countryMapper.toEntity(request);
        return countryMapper.toResponse(countryRepository.save(country));
    }

    @Override
    @Transactional
    public CountryResponse update(Long id, CountryRequest request) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country no encontrado con id: " + id));
        countryMapper.updateEntity(country, request);
        return countryMapper.toResponse(countryRepository.save(country));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country no encontrado con id: " + id));
        if (Boolean.TRUE.equals(country.getEnabled())) {
            throw new DuplicateResourceException("El país ya está habilitado");
        }
        country.setEnabled(true);
        countryRepository.save(country);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country no encontrado con id: " + id));
        country.setEnabled(false);
        countryRepository.save(country);
    }

}
