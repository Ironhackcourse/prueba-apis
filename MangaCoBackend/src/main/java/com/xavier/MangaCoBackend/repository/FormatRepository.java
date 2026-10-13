package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormatRepository extends JpaRepository<Format, Long> {

    Optional<Format> findByNameFormatIgnoreCase(String nameFormat);

    List<Format> findAllByEnabledTrue();

    List<Format> findAllByEnabledFalse();

    boolean existsByNameFormatIgnoreCase(String nameFormat);
}