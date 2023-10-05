package com.glorified.spotifygapi.repository.tracks;

import com.glorified.spotifygapi.models.track.Track;
import com.glorified.spotifygapi.models.track.TrackFeatures;
import com.glorified.spotifygapi.repository.SqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrackRepoImpl extends SqlAbstractRepository implements TrackRepository {

    @Override
    public List<TrackFeatures> insertTrackFeatures(List<TrackFeatures> trackFeatures, List<String> ids) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            connection = this.newConnection();

            //temporary
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM track_features"
            );
            preparedStatement.executeUpdate();
            String[] generatedColumns = {"id"};

            for(TrackFeatures tf:trackFeatures) {
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO track_features (track_id, analysis_id, acousticness, dancebility, energy, instrumentalness, key_tone, liveness, loudness, modality, speechiness, bpm, time_signature, valence, href) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                        ,generatedColumns
                );
                preparedStatement.setString(1,tf.getTrackID());
                preparedStatement.setString(2,tf.getAnalysisID());
                preparedStatement.setDouble(3,tf.getAccousticness());
                preparedStatement.setDouble(4,tf.getDancebility());
                preparedStatement.setDouble(5,tf.getEnergy());
                preparedStatement.setDouble(6,tf.getInstrumentalness());
                preparedStatement.setDouble(7,tf.getKey());
                preparedStatement.setDouble(8,tf.getLiveness());
                preparedStatement.setDouble(9,tf.getLoudness());
                preparedStatement.setDouble(10,tf.getModality());
                preparedStatement.setDouble(11,tf.getSpeechiness());
                preparedStatement.setDouble(12,tf.getBpm());
                preparedStatement.setDouble(13,tf.getTimeSignature());
                preparedStatement.setDouble(14,tf.getValence());
                preparedStatement.setString(15,tf.getHref());

                preparedStatement.executeUpdate();

            }
            List<TrackFeatures> listFeatures = new ArrayList<>();
            for(String id:ids) {
                preparedStatement = connection.prepareStatement("SELECT * FROM track_features WHERE track_id = ?");
                preparedStatement.setString(1, id);
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    TrackFeatures tf = new TrackFeatures();
                    tf.setId(resultSet.getInt("id"));
                    tf.setTrackID(resultSet.getString("track_id"));
                    tf.setAnalysisID(resultSet.getString("analysis_id"));
                    tf.setAccousticness(resultSet.getDouble("acousticness"));
                    tf.setDancebility(resultSet.getDouble("dancebility"));
                    tf.setEnergy(resultSet.getDouble("energy"));
                    tf.setInstrumentalness(resultSet.getDouble("instrumentalness"));
                    tf.setKey(resultSet.getDouble("key_tone"));
                    tf.setLiveness(resultSet.getDouble("liveness"));
                    tf.setLoudness(resultSet.getDouble("loudness"));
                    tf.setModality(resultSet.getDouble("modality"));
                    tf.setSpeechiness(resultSet.getDouble("speechiness"));
                    tf.setBpm(resultSet.getDouble("bpm"));
                    tf.setTimeSignature(resultSet.getDouble("time_signature"));
                    tf.setValence(resultSet.getDouble("valence"));
                    tf.setHref(resultSet.getString("href"));

                    listFeatures.add(tf);
                }
            }
            return listFeatures;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

