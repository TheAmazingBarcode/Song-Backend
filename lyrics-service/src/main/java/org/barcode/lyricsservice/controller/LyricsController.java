package org.barcode.lyricsservice.controller;

import org.barcode.lyricsservice.dto.SearchDTO;
import org.barcode.lyricsservice.model.Lyrics;
import org.barcode.lyricsservice.service.LyricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("lyrics")
public class LyricsController {

    @Autowired
    private LyricsService lyricsService;

    @PostMapping(value = "new", consumes = "application/json")
    public ResponseEntity<String> newLyrics(@RequestBody Lyrics lyrics) {
        return ResponseEntity.ok(lyricsService.createLyrics(lyrics));
    }

    @GetMapping(value = "id/{lyricsID}")
    public ResponseEntity<Lyrics> lyricsByID(@PathVariable("lyricsID") String lyricsID) {
        return ResponseEntity.ok(lyricsService.getLyricsByID(lyricsID));
    }

    @PostMapping(value = "category")
    public ResponseEntity<List<Lyrics>> lyricsByGenre(@RequestBody List<SearchDTO> searchDTOList) {
        return ResponseEntity.ok(lyricsService.getLyricsByCriteria(searchDTOList));
    }

    @GetMapping(value = "all")
    public ResponseEntity<List<Lyrics>> allLyrics() throws IOException {
        return ResponseEntity.ok(lyricsService.getAllLyrics());
    }

    @PutMapping(value = "update", consumes = "application/json")
    public ResponseEntity<Lyrics> updateLyrics(@RequestBody Lyrics lyrics) {
        return ResponseEntity.ok(lyricsService.updateLyrics(lyrics));
    }

    @DeleteMapping(value = "delete/{lyricsID}")
    public ResponseEntity<Boolean> deleteLyrics(@PathVariable("lyricsID") String id) {
        return ResponseEntity.ok(lyricsService.deleteLyrics(id));
    }
}
