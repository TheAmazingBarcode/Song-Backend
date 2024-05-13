package org.barcode.songservice.controller;

import org.barcode.songservice.model.Genre;
import org.barcode.songservice.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("genre")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @GetMapping("all")
    public ResponseEntity<List<Genre>> getAllGenres(){
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @DeleteMapping("delete/{genreId}")
    public ResponseEntity<Boolean> deleteGenre(@PathVariable("genreId") Integer id){
        return ResponseEntity.ok(genreService.deleteGenre(id));
    }
}
