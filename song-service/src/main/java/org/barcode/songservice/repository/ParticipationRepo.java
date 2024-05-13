package org.barcode.songservice.repository;

import org.barcode.songservice.model.Participation;
import org.barcode.songservice.model.Performer;
import org.barcode.songservice.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepo extends JpaRepository<Participation,Integer> {
    void deleteParticipationsBySongPerformerSource(Song song);

    List<Participation> findParticipationsBySongPerformer(Performer songPerformer);
}
