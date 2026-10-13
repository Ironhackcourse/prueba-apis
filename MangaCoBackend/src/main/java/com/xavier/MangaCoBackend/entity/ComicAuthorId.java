package com.xavier.MangaCoBackend.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ComicAuthorId implements Serializable {

    private Long comic;
    private Long author;
}