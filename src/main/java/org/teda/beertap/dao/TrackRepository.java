package org.teda.beertap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teda.beertap.entity.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

}
