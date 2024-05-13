package org.barcode.songservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "performer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Performer {
    @Id
    @Column(name = "performer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "artist_name")
    private String artistName;
}
