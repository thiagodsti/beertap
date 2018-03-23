package org.teda.beertap.rest;

import org.teda.beertap.entity.Track;

import java.util.List;

public class TrackBatchRequest {

    private String email;
    private List<Track> tracks;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
