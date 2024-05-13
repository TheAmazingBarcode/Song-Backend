package org.barcode.songservice.controller;

import org.barcode.songservice.model.Performer;
import org.barcode.songservice.service.PerformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("performer")
public class PerformerController {
    @Autowired
    private PerformerService performerService;

    @GetMapping("all")
    public ResponseEntity<List<Performer>> getAllPerformers(){
        return ResponseEntity.ok(performerService.getAllPerformers());
    }

}
