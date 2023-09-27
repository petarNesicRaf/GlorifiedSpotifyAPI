package com.glorified.spotifygapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glorified.spotifygapi.models.playlist.Playlist;
import com.glorified.spotifygapi.repository.playlist.PlaylistRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

    @Inject
    PlaylistRepository playlistRepository;

    public void deleteInsertPlaylist(String response)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonResponse = objectMapper.readTree(response);
            List<JsonNode> items = jsonResponse.get("items").findValues("items");
            List<Playlist> playlists = new ArrayList<>();
            for(JsonNode item:items)
            {
                Playlist playlist = new Playlist();
                playlist.setName(item.get("name").asText());
                playlist.setDescription(item.get("description").asText());
                playlist.setPlaylistId(item.get("id").asText());
                playlist.setOwnerName(item.get("owner").get("display_name").asText());
                playlist.setUserId(item.get("owner").get("id").asText());
                playlist.setTracks(item.get("tracks").get("href").asText());
                playlist.setNumOfTracks(item.get("tracks").get("total").asInt());
                playlist.setImageLarge(item.get("images").get(0).get("url").asText());
                playlist.setImageMedium(item.get("images").get(1).get("url").asText());
                playlist.setImageSmall(item.get("images").get(2).get("url").asText());
                if(item.get("collaborative").asBoolean())
                    playlist.setCollaborative(1);
                else    playlist.setCollaborative(0);

                playlists.add(playlist);
            }

            this.playlistRepository.deleteAndInsert(playlists);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
