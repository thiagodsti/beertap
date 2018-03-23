package org.teda.beertap.service;

import org.teda.beertap.entity.Track;
import org.teda.beertap.rest.TrackBatchRequest;

import java.util.List;

public interface TrackService {

    Track save(Track track);

    List<Long> saveBatch(TrackBatchRequest tracks);

}
