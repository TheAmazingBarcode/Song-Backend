package org.barcode.songservice.repository;

import org.barcode.songservice.model.Author;
import org.barcode.songservice.model.Song;
import org.barcode.songservice.model.SongWriter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongWriterRepo extends JpaRepository<SongWriter,Integer> {
    void deleteSongWritersBySongWriterSource(Song entity);

    List<SongWriter> findSongWritersBySongWriter(Author songWriter);
}
