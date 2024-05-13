package org.barcode.songservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "performer-participation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Participation {
    @JsonIgnore
    @Id
    @Column(name = "participation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "performer_performer_id")
    private Performer songPerformer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "song_song_id")
    private Song songPerformerSource;
}
