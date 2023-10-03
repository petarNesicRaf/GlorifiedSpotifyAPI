package com.glorified.spotifygapi.repository.playlist;

import com.glorified.spotifygapi.models.playlist.Playlist;
import com.glorified.spotifygapi.repository.SqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistRepoImpl extends SqlAbstractRepository implements PlaylistRepository {
    @Override
    public void deleteAndInsert(List<Playlist> playlists) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement(
                    "DELETE FROM playlists"
            );
            preparedStatement.executeUpdate();
            String[] generatedColumns = {"id"};

            for(Playlist playlist:playlists) {
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO playlists (playlist_name, description, playlist_id, user_id, owner_name, tracks, num_of_tracks, imageLarge, imageMedium, imageSmall, collaborative) VALUES(?,?,?,?,?,?,?,?,?,?,?)"
                            ,generatedColumns
                );
                preparedStatement.setString(1,playlist.getName());
                preparedStatement.setString(2,playlist.getDescription());
                preparedStatement.setString(3,playlist.getPlaylistId());
                preparedStatement.setString(4,playlist.getUserId());
                preparedStatement.setString(5,playlist.getOwnerName());
                preparedStatement.setString(6,playlist.getTracks());
                preparedStatement.setInt(7,playlist.getNumOfTracks());
                preparedStatement.setString(8,playlist.getImageLarge());
                preparedStatement.setString(9,playlist.getImageMedium());
                preparedStatement.setString(10,playlist.getImageSmall());
                preparedStatement.setInt(11,playlist.getCollaborative());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Playlist> allPlaylists = new ArrayList<>();

        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT * FROM playlists");
            while(resultSet.next())
            {
                allPlaylists.add(new Playlist(
                        resultSet.getInt("id"),
                        resultSet.getString("playlist_name"),
                        resultSet.getString("description"),
                        resultSet.getString("playlist_id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("owner_name"),
                        resultSet.getString("tracks"),
                        resultSet.getInt("num_of_tracks"),
                        resultSet.getString("imageLarge"),
                        resultSet.getString("imageMedium"),
                        resultSet.getString("imageSmall"),
                        resultSet.getInt("collaborative")));
            }
            return allPlaylists;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPlaylistID(String playlistName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = this.newConnection().prepareStatement("SELECT tracks FROM playlists WHERE playlist_name = ?");
            statement.setString(1, playlistName);
            resultSet = statement.executeQuery();
            String playlistID = "";
            while(resultSet.next())
            {
                playlistID = resultSet.getString("tracks");
            }

            return playlistID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
