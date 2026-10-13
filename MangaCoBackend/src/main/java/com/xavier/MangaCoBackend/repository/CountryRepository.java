package com.xavier.MangaCoBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xavier.MangaCoBackend.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByNameCountryIgnoreCase(String nameCountry);

    List<Country> findAllByEnabledTrue();

    List<Country> findAllByEnabledFalse();

    List<Country> findByEnabledTrue();

    boolean existsByNameCountryIgnoreCase(String nameCountry);

}
