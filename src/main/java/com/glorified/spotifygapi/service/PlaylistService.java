package com.glorified.spotifygapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glorified.spotifygapi.models.playlist.Playlist;
import com.glorified.spotifygapi.models.track.Track;
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
            //prebaci ovo u posebnu metodu za deserijalizaciju
            for(JsonNode item:items) {
                Playlist playlist = deserializePlaylists(item);
                playlists.add(playlist);
            }
            this.playlistRepository.deleteAndInsert(playlists);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Track> insertTracksFromPlaylist(String response)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode tree = objectMapper.readTree(response);
            JsonNode items = tree.get("items");
            List<Track> tracks = new ArrayList<>();

            for(JsonNode item:items)
            {
                Track track = deserializeTracks(item);
                tracks.add(track);
            }

            return playlistRepository.insertTracksFromPlaylist(tracks);

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

    private Playlist deserializePlaylists(JsonNode item)
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

        return playlist;
        //playlists.add(playlist);
    }



    private Track deserializeTracks(JsonNode item)
    {
        Track track = new Track();
        track.setAddedAt(item.get("added_at").asText());
        track.setAddedByHref(item.get("added_by").get("href").asText());
        track.setAddedByID(item.get("added_by").get("id").asText());
        track.setDuration(item.get("track").get("duration_ms").asText());
        track.setTrackHref(item.get("track").get("href").asText());
        track.setTrackName(item.get("track").get("name").asText());
        track.setIsLocal(item.get("track").get("is_local").asInt());
        track.setTrackID(item.get("track").get("id").asText());
        track.setPopularity(item.get("track").get("popularity").asInt());
        //moze da bude null
        track.setVideoThumbnail(item.get("video_thumbnail").get("url").asText());
        track.setPreviewUrl(item.get("track").get("preview_url").asText());
        //ovde za dodavanje artista sa zarezom mora da se nadje bolja solucija ovo je glupost
        JsonNode artists = item.get("track").get("artists");
        String artistName = "";
        String artistID = "";
        for(JsonNode artist :artists)
        {
            artistName.concat(artist.get("name").asText());
            artistName.concat(",");

            artistID.concat(artist.get("id").asText());
            artistID.concat(",");
        }

        track.setArtistsNames(artistName);
        track.setArtistsIDs(artistID);
        track.setAlbumName(item.get("track").get("album").get("name").asText());
        track.setAlbumID(item.get("track").get("album").get("id").asText());
        track.setAlbumType(item.get("track").get("album").get("type").asText());

        JsonNode images = item.get("track").get("album").get("images");
        if(images != null && images.isArray()) {
            JsonNode firstImageNode = images.size() > 0 ? images.get(0) : null;
            JsonNode secondImageNode = images.size() > 1 ? images.get(1) : null;
            JsonNode thirdImageNode = images.size() > 2 ? images.get(2) : null;

            track.setImageLarge(
                    getImageUrlOrDefault(firstImageNode, "large")
            );

            track.setImageMedium(
                    getImageUrlOrDefault(secondImageNode, "medium")
            );

            track.setImageSmall(
                    getImageUrlOrDefault(thirdImageNode, "small")
            );
        }else{
            track.setImageLarge("null");
            track.setImageMedium("null");
            track.setImageSmall("null");
        }

        return track;
    }


}
