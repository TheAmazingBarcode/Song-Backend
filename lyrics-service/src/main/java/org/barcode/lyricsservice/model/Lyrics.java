package org.barcode.lyricsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class Lyrics {

    private String id;
    private String name;
    private String lyrics;
    private List<String> names;
    private String genre;

    public Lyrics setId(String id) {
        this.id = id;
        return this;
    }

    public Lyrics setNames(List<String> names){
        this.names = names;
        return this;
    }

    public Lyrics setGenre(String genre){
        this.genre = genre;
        return this;
    }
}
