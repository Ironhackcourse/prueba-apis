package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Long> {

    Optional<Classification> findByNameClassificationIgnoreCase(String nameClassification);

    List<Classification> findAllByEnabledTrue();

    List<Classification> findAllByEnabledFalse();

    boolean existsByNameClassificationIgnoreCase(String nameClassification);
}