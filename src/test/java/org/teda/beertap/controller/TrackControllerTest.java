package org.teda.beertap.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.teda.beertap.ApplicationTest;
import org.teda.beertap.entity.Track;
import org.teda.beertap.entity.User;
import org.teda.beertap.factory.TrackFactory;
import org.teda.beertap.factory.UserFactory;
import org.teda.beertap.rest.TrackBatchRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TrackControllerTest extends ApplicationTest {

    @Autowired
    private TrackFactory factory;

    @Autowired
    private UserFactory userFactory;

    @Test
    public void save() {
        Track track = factory.create("/");
        User user = userFactory.createAndSave("test@track.com");
        track.setUser(user);
        ResponseEntity<Long> response = restTemplate.postForEntity("/tracks", track, Long.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo(1L));
    }

    @Test
    public void saveUserNullException() {
        Track track = factory.create("/");
        User user = userFactory.create("test@track.com");
        ResponseEntity<String> response = restTemplate.postForEntity("/tracks", track, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody(), containsString("User cannot be null"));
    }

    @Test
    public void saveBatch() {
        List<Track> tracks = buildTracks();
        User user = userFactory.createAndSave("test2@track.com");
        TrackBatchRequest request = createTrackBatchRequest(tracks, user);
        ResponseEntity<Long[]> response = restTemplate.postForEntity("/tracks/batch", request, Long[].class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody().length, equalTo(2));
    }

    @Test
    public void saveBatchUserDoesntExistsException() {
        List<Track> tracks = buildTracks();
        User user = userFactory.create("test3@track.com");
        TrackBatchRequest request = createTrackBatchRequest(tracks, user);
        ResponseEntity<String> response = restTemplate.postForEntity("/tracks/batch", request, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody(), containsString("User doesn't exist"));
    }

    private TrackBatchRequest createTrackBatchRequest(List<Track> tracks, User user) {
        TrackBatchRequest request = new TrackBatchRequest();
        request.setTracks(tracks);
        request.setEmail(user.getEmail());
        return request;
    }

    private List<Track> buildTracks() {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(factory.create("/about"));
        tracks.add(factory.create("/contacts"));
        return tracks;
    }

}
