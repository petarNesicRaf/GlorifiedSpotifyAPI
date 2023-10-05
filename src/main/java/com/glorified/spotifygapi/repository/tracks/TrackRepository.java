package com.glorified.spotifygapi.repository.tracks;

import com.glorified.spotifygapi.models.track.TrackFeatures;

import java.util.List;

public interface TrackRepository {
    public List<TrackFeatures> insertTrackFeatures(List<TrackFeatures> trackFeatures, List<String> ids);
}
