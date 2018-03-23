package org.teda.beertap.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.teda.beertap.ApplicationTest;
import org.teda.beertap.entity.Track;
import org.teda.beertap.entity.User;
import org.teda.beertap.factory.UserFactory;
import org.teda.beertap.rest.TrackBatchRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class UserControllerTest extends ApplicationTest {

    @Autowired
    private UserFactory factory;

    @Test
    public void save() {
        User user = factory.create("test@test.com");
        ResponseEntity<Long> response = restTemplate.postForEntity("/users", user, Long.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void findAll() {
        User user = factory.create("test2@test.com");
        ResponseEntity<Long> id = restTemplate.postForEntity("/users", user, Long.class);
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().length, greaterThanOrEqualTo(1));
        boolean exist = false;
        for (User u : response.getBody()) {
            if (u.getId().equals(id.getBody())) {
                exist = true;
                break;
            }
        }
        assertTrue(exist);
    }

    @Test
    public void findTracks() {
        User user = factory.create("test3@test.com");
        ResponseEntity<Long> id = restTemplate.postForEntity("/users", user, Long.class);
        user.setId(id.getBody());
        List<Track> tracks = buildTracks();
        TrackBatchRequest request = new TrackBatchRequest();
        request.setEmail(user.getEmail());
        request.setTracks(tracks);
        ResponseEntity<Long[]> response = restTemplate.postForEntity("/tracks/batch", request, Long[].class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        ResponseEntity<Track[]> responseTrack = restTemplate.getForEntity("/users/" + user.getEmail() + "/tracks", Track[].class);
        assertThat(responseTrack.getStatusCode(), equalTo(HttpStatus.OK));
        Track[] tracksArray = responseTrack.getBody();
        List<String> uris = Stream.of(tracksArray).map(Track::getUri).collect(toList());
        List<Track> tracksExists = tracks.stream().filter(t -> !uris.contains(t.getUri())).collect(toList());
        assertThat(tracksExists.size(), equalTo(0));
    }

    private List<Track> buildTracks() {
        Track track = new Track();
        track.setDate(new Date().getTime());
        track.setUri("/about");
        Track track2 = new Track();
        track2.setDate(new Date().getTime());
        track2.setUri("/contacts");

        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(track);
        tracks.add(track2);
        return tracks;
    }

}
