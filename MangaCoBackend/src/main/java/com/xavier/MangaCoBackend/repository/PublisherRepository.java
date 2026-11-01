package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByNamePublisherIgnoreCase(String namePublisher);

    List<Publisher> findAllByEnabledTrue();

    List<Publisher> findAllByEnabledFalse();

    List<Publisher> findByCountry_Id(Long countryId);

    boolean existsByNamePublisherIgnoreCase(String namePublisher);
}