package org.teda.beertap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teda.beertap.entity.Track;
import org.teda.beertap.rest.TrackBatchRequest;
import org.teda.beertap.service.TrackService;

import java.util.List;

@RestController
@RequestMapping( value = "tracks", produces = MediaType.APPLICATION_JSON_VALUE )
public class TrackController {

    @Autowired
    private TrackService service;

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody Track track) {
        Long id = service.save(track).getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping(value = "batch", consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<Long>> saveBatch(@RequestBody TrackBatchRequest trackBatchRequest) {
        List<Long> ids = service.saveBatch(trackBatchRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ids);
    }

}
