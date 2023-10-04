package com.glorified.spotifygapi.repository.playlist;

import com.glorified.spotifygapi.models.playlist.Playlist;
import com.glorified.spotifygapi.models.track.Track;

import java.util.List;

public interface PlaylistRepository {

    public void deleteAndInsert(List<Playlist> listPlaylist);

    public List<Playlist> getAllPlaylists();

    public String getPlaylistID(String playlistName);

    public List<Track> insertTracksFromPlaylist(List<Track> tracks);
}
