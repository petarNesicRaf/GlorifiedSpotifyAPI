package com.glorified.spotifygapi.repository.playlist;

import com.glorified.spotifygapi.models.playlist.Playlist;
import com.glorified.spotifygapi.models.track.Track;
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

    @Override
    public List<Track> insertTracksFromPlaylist(List<Track> tracks) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            connection = this.newConnection();
            //temporary
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM tracks"
            );
            preparedStatement.executeUpdate();
            String[] generatedColumns = {"id"};

            for(Track track:tracks) {
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO tracks (added_at, added_by_href, added_by_id, duration, track_href, track_id, is_local, track_name, popularity, video_thumbnail, preview_url, artists_names, artists_ids, album_type, album_name, album_id, image_large, image_medium, image_small) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                        ,generatedColumns
                );
                preparedStatement.setString(1,track.getAddedAt());
                preparedStatement.setString(2,track.getAddedByHref());
                preparedStatement.setString(3,track.getAddedByID());
                preparedStatement.setString(4,track.getDuration());
                preparedStatement.setString(5,track.getTrackHref());
                preparedStatement.setString(6,track.getTrackID());
                preparedStatement.setInt(7,track.getIsLocal());
                preparedStatement.setString(8,track.getTrackName());
                preparedStatement.setInt(9,track.getPopularity());
                preparedStatement.setString(10,track.getVideoThumbnail());
                preparedStatement.setString(11,track.getPreviewUrl());
                preparedStatement.setString(12,track.getArtistsNames());
                preparedStatement.setString(13,track.getArtistsIDs());
                preparedStatement.setString(14,track.getAlbumType());
                preparedStatement.setString(15,track.getAlbumName());
                preparedStatement.setString(16,track.getAlbumID());
                preparedStatement.setString(17,track.getImageLarge());
                preparedStatement.setString(18,track.getImageMedium());
                preparedStatement.setString(19,track.getImageSmall());


                preparedStatement.executeUpdate();
            }
            return tracks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
