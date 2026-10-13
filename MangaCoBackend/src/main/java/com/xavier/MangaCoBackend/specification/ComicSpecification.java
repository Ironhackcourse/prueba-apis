package com.xavier.MangaCoBackend.specification;

import com.xavier.MangaCoBackend.entity.Comic;
import org.springframework.data.jpa.domain.Specification;

public class ComicSpecification {

    private ComicSpecification() {
        // clase utilitaria, no instanciable
    }

    public static Specification<Comic> isEnabled(Boolean enabled) {
        if (enabled == null) {
            return (root, query, cb) -> cb.equal(root.get("enabled"), true); // por defecto, activos
        }
        return (root, query, cb) -> cb.equal(root.get("enabled"), enabled);
    }

    public static Specification<Comic> hasSeries(Long seriesId) {
        return (root, query, cb) ->
                seriesId == null ? null : cb.equal(root.get("series").get("id"), seriesId);
    }

    public static Specification<Comic> hasPublisher(Long publisherId) {
        return (root, query, cb) ->
                publisherId == null ? null : cb.equal(root.get("publisher").get("id"), publisherId);
    }

    public static Specification<Comic> hasCountry(Long countryId) {
        return (root, query, cb) ->
                countryId == null ? null : cb.equal(root.get("country").get("id"), countryId);
    }

    public static Specification<Comic> hasFormat(Long formatId) {
        return (root, query, cb) ->
                formatId == null ? null : cb.equal(root.get("format").get("id"), formatId);
    }

    public static Specification<Comic> titleContains(String title) {
        return (root, query, cb) ->
                (title == null || title.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
}