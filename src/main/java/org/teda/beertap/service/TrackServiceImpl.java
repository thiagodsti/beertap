package org.teda.beertap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teda.beertap.dao.TrackRepository;
import org.teda.beertap.entity.Track;
import org.teda.beertap.entity.User;
import org.teda.beertap.rest.TrackBatchRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackRepository repository;

    @Autowired
    private UserService userService;

    @Override
    public Track save(Track track) {
        validate(track);
        String email = track.getUser().getEmail();
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User doesn't exists in this database"));
        track.setUser(user);
        return repository.save(track);
    }

    @Override
    public List<Long> saveBatch(TrackBatchRequest request) {
        String email = request.getEmail();
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User doesn't exist"));
        List<Long> ids = new ArrayList<>();
        for(Track t : request.getTracks()) {
            t.setUser(user);
            ids.add(repository.save(t).getId());
        }
        return ids;
    }

    private void validate(Track track) {
        if (track.getUser() == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

}
