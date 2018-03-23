package org.teda.beertap.service;

import org.teda.beertap.entity.Track;
import org.teda.beertap.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    List<User> findAll();

    Optional<User> findByEmail(String email);

    List<Track> findTracksByEmail(String email);
}
