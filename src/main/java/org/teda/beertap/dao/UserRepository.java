package org.teda.beertap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.teda.beertap.entity.Track;
import org.teda.beertap.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOptionalByEmail(String email);

    @Query("SELECT t FROM Track t INNER JOIN t.user u WHERE u.email LIKE :email")
    List<Track> findTracksByEmail(@Param("email") String email);

}
