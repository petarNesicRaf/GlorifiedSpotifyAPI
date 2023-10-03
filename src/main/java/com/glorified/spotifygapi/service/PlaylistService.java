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
            JsonNode items = jsonResponse.get("items");
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
                JsonNode images = item.get("images");
                if(images != null && images.isArray()) {
                    JsonNode firstImageNode = images.size() > 0 ? images.get(0) : null;
                    JsonNode secondImageNode = images.size() > 1 ? images.get(1) : null;
                    JsonNode thirdImageNode = images.size() > 2 ? images.get(2) : null;

                    playlist.setImageLarge(
                            getImageUrlOrDefault(firstImageNode, "large")
                    );

                    playlist.setImageMedium(
                            getImageUrlOrDefault(secondImageNode, "medium")
                    );

                    playlist.setImageSmall(
                            getImageUrlOrDefault(thirdImageNode, "small")
                    );
                }else{
                    playlist.setImageLarge("null");
                    playlist.setImageMedium("null");
                    playlist.setImageSmall("null");
                }

                if(item.get("collaborative").asBoolean())
                    playlist.setCollaborative(1);
                else    playlist.setCollaborative(0);

                System.out.println("Name: " + playlist.getName());
                System.out.println("Description: " + playlist.getDescription());
                System.out.println("ID: " + playlist.getId());

                playlists.add(playlist);
            }

            this.playlistRepository.deleteAndInsert(playlists);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    private String getImageUrlOrDefault(JsonNode imageNode, String size) {
        if (imageNode != null && !imageNode.isMissingNode() && imageNode.has("url")) {
            return imageNode.get("url").asText();
        } else {
            return "null"; // Or set a default URL or handle it as needed
        }
    }

    public List<Playlist> getAllPlaylist()
    {
        return this.playlistRepository.getAllPlaylists();
    }

    public String getPlaylistID(String playlistName)
    {
       return playlistRepository.getPlaylistID(playlistName);
    }
}
