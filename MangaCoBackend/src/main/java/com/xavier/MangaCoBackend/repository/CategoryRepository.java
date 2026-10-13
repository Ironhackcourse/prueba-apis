package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameCategoryIgnoreCase(String nameCategory);

    List<Category> findAllByEnabledTrue();

    List<Category> findAllByEnabledFalse();

    boolean existsByNameCategoryIgnoreCase(String nameCategory);
}