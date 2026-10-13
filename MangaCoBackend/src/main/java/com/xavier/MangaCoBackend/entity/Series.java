package com.xavier.MangaCoBackend.entity;

import com.xavier.MangaCoBackend.enums.SeriesStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_series")
    private Long id;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "original_title", length = 250)
    private String originalTitle;

    @Column(name = "synopsis", columnDefinition = "TEXT")
    private String synopsis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_id")
    private Classification classification;

    @Column(name = "total_volumes")
    private Integer totalVolumes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private SeriesStatus status = SeriesStatus.ONGOING;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.enabled = (this.enabled == null) ? true : this.enabled;
        this.status = (this.status == null) ? SeriesStatus.ONGOING : this.status;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}