package org.barcode.songservice.repository;

import org.barcode.songservice.model.Genre;
import org.barcode.songservice.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepo extends JpaRepository<Song,Integer> {
    List<Song> findSongsByGenre(Genre genre);

    @Query("SELECT s FROM Song s WHERE s.lyricsId IN :ids")
    List<Song> findSongsByLyricsIds(List<String> ids);
}
