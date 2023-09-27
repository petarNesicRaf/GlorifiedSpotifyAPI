package com.glorified.spotifygapi.repository.playlist;

import com.glorified.spotifygapi.models.playlist.Playlist;

import java.util.List;

public interface PlaylistRepository {

    public void deleteAndInsert(List<Playlist> listPlaylist);

    public List<Playlist> getAllPlaylists();

}
