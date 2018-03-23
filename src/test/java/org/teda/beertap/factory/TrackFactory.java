package org.teda.beertap.factory;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Component;
import org.teda.beertap.ApplicationTest;
import org.teda.beertap.entity.Track;

import java.util.Date;
import java.util.List;

@Component
public class TrackFactory extends ApplicationTest {

    public Track create(String uri) {
        Track track = new Track();
        track.setDate(new Date().getTime());
        track.setUri(uri);
        return track;
    }




}
