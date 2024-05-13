package org.barcode.lyricsservice.controller;

import org.barcode.lyricsservice.dto.SearchDTO;
import org.barcode.lyricsservice.model.Lyrics;
import org.barcode.lyricsservice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<List<Lyrics>> searchForLyrics(@RequestBody List<SearchDTO> searchDTOList){
        return ResponseEntity.ok(searchService.search(searchDTOList));
    }
}
