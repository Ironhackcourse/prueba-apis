package com.xavier.MangaCoBackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comic_author")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ComicAuthorId.class)
public class ComicAuthor {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comic_id", nullable = false)
    private Comic comic;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "author_role", nullable = false, length = 30)
    @Builder.Default
    private String authorRole = "writer";
}