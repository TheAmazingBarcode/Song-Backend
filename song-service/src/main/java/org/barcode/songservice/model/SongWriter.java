package org.barcode.songservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "song-author")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongWriter {
    @JsonIgnore
    @Id
    @Column(name = "song-author_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "author_author_id")
    private Author songWriter;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "song_song_id")
    private Song songWriterSource;
}
