package org.barcode.songservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "song")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    @Column(name = "song_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "lyrics_id")
    private String lyricsId;

    @ManyToOne
    @JoinColumn(name = "genre_genre_id")
    private Genre genre;

    @OneToMany(mappedBy = "songPerformerSource")
    private Set<Participation> performers;

    @OneToMany(mappedBy = "songWriterSource")
    private Set<SongWriter> authors;
}
