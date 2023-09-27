package com.glorified.spotifygapi.repository.playlist;

import com.glorified.spotifygapi.models.playlist.Playlist;
import com.glorified.spotifygapi.repository.SqlAbstractRepository;

import java.util.List;

public class PlaylistRepoImpl extends SqlAbstractRepository implements PlaylistRepository {
    @Override
    public void deleteAndInsert(List<Playlist> playlists) {

    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return null;
    }
}
