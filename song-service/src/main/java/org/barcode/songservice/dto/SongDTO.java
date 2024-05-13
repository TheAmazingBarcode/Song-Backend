package org.barcode.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.barcode.songservice.model.Author;
import org.barcode.songservice.model.Genre;
import org.barcode.songservice.model.Performer;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    private Integer id;

    private String name;

    private String lyrics;

    private String lyricsID;

    private Genre genre;

    private Set<Author> authors;

    private Set<Performer> performers;
}
