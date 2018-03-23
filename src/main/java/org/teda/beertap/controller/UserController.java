package org.teda.beertap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.teda.beertap.entity.Track;
import org.teda.beertap.entity.User;
import org.teda.beertap.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping( value = "users", produces = MediaType.APPLICATION_JSON_VALUE )
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody User user) {
        Long id = service.save(user).getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = service.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{email}/tracks")
    public ResponseEntity<List<Track>> findTracks(@PathVariable("email") String email) {
        List<Track> tracks = service.findTracksByEmail(email);
        return ResponseEntity.ok(tracks);
    }

}
