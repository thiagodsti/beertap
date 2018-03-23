package org.teda.beertap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teda.beertap.dao.UserRepository;
import org.teda.beertap.entity.Track;
import org.teda.beertap.entity.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findOptionalByEmail(email);
    }

    @Override
    public List<Track> findTracksByEmail(String email) {
        return repository.findTracksByEmail(email);
    }
}
