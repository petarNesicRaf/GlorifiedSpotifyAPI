package com.glorified.spotifygapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glorified.spotifygapi.models.track.TrackFeatures;
import com.glorified.spotifygapi.repository.tracks.TrackRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TrackService {
    @Inject
    TrackRepository trackRepository;

    public List<TrackFeatures> insertTrackFeatures(String trackFeatures, List<String> trackIDs)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode features = objectMapper.readTree(trackFeatures).get("audio_features");
            List<TrackFeatures> tf = new ArrayList<>();
            //for(String trackID: trackIDs) {
            int i = 0;
                for (JsonNode feature : features) {
                    TrackFeatures track = deserializeFeature(feature, trackIDs.get(i));
                    tf.add(track);
                    i++;
                }
            //}
            return trackRepository.insertTrackFeatures(tf, trackIDs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private TrackFeatures deserializeFeature(JsonNode feature, String trackID)
    {
        TrackFeatures trackFeatures = new TrackFeatures();
        trackFeatures.setTrackID(trackID);
        trackFeatures.setAnalysisID(feature.get("id").asText());
        trackFeatures.setAccousticness(feature.get("acousticness").asDouble());
        trackFeatures.setDancebility(feature.get("danceability").asDouble());
        trackFeatures.setEnergy(feature.get("energy").asDouble());
        trackFeatures.setInstrumentalness(feature.get("instrumentalness").asDouble());
        trackFeatures.setKey(feature.get("key").asDouble());
        trackFeatures.setLiveness(feature.get("liveness").asDouble());
        trackFeatures.setLoudness(feature.get("loudness").asDouble());
        trackFeatures.setModality(feature.get("mode").asDouble());
        trackFeatures.setSpeechiness(feature.get("speechiness").asDouble());
        trackFeatures.setBpm(feature.get("tempo").asDouble());
        trackFeatures.setTimeSignature(feature.get("time_signature").asDouble());
        trackFeatures.setValence(feature.get("valence").asDouble());
        trackFeatures.setHref(feature.get("track_href").asText());
        return trackFeatures;
    }
}
