package org.barcode.cacheservice.controller;

import org.barcode.cacheservice.model.LyricsDto;
import org.barcode.cacheservice.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    private CacheService service;

    @GetMapping(value = "all")
    public ResponseEntity<List<LyricsDto>> getAllSongs(){
        return ResponseEntity.ok(service.getAllSongs());
    }
}
