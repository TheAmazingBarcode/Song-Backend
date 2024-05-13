package org.barcode.songservice.controller;

import org.barcode.songservice.dto.SongDTO;
import org.barcode.songservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("song")
public class SongController {
    @Autowired
    private SongService songService;

    @GetMapping(value = "all")
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @GetMapping(value = "genre/{genreName}")
    public ResponseEntity<List<SongDTO>> getSongsByGenre(@PathVariable("genreName") String name) {
        return ResponseEntity.ok(songService.getSongsByGenre(name));
    }

    @GetMapping(value = "author/{authorId}")
    public ResponseEntity<List<SongDTO>> getSongsByAuthor(@PathVariable("authorId") Integer id){
        return ResponseEntity.ok(songService.getSongsByAuthor(id));
    }

    @GetMapping(value = "performer/{performerId}")
    public ResponseEntity<List<SongDTO>> getSongsByPerformer(@PathVariable("performerId") Integer id){
        return ResponseEntity.ok(songService.getSongsByPerformer(id));
    }

    @PostMapping(value = "upload", consumes = "application/json")
    public ResponseEntity<SongDTO> uploadSong(@RequestBody SongDTO songDTO) {
        return ResponseEntity.ok(songService.newSong(songDTO));
    }

    @PostMapping(value = "search",consumes = "application/json")
    public ResponseEntity<List<SongDTO>> searchSongsByIds(@RequestBody List<String> ids){
        return ResponseEntity.ok(songService.searchSongs(ids));
    }

    @PutMapping(value = "update", consumes = "application/json")
    public ResponseEntity<SongDTO> updateSong(@RequestBody SongDTO songDTO) {
        return ResponseEntity.ok(songService.updateSong(songDTO));
    }

    @DeleteMapping(value = "delete/{songID}")
    public ResponseEntity<Boolean> deleteSong(@PathVariable("songID") Integer id) {
        return ResponseEntity.ok(songService.deleteSong(id));
    }
}
