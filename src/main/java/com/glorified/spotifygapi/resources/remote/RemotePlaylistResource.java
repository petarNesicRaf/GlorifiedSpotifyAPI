package com.glorified.spotifygapi.resources.remote;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glorified.spotifygapi.models.playlist.Playlist;
import com.glorified.spotifygapi.resources.AuthenticationResource;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/remote-playlist")
public class RemotePlaylistResource {

    @GET
    public Response fetchCurrentUserPlaylists(@QueryParam("access") String accessToken) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/me/playlists?limit=50&offset=0");
        //httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationResource.tokenManager.getToken().getAccessToken());
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(httpResponse.getEntity());

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                JsonNode items = jsonResponse.get("items");
                List<Playlist> playlists = new ArrayList<>();

                for(JsonNode item:items) {
                    Playlist playlist = deserializePlaylists(item);
                    playlists.add(playlist);
                }

                return Response.ok(playlists, MediaType.APPLICATION_JSON).build();
            }else{
                return Response.status(httpResponse.getStatusLine().getStatusCode()).build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    private String getImageUrlOrDefault(JsonNode imageNode, String size) {
        if (imageNode != null && !imageNode.isMissingNode() && imageNode.has("url")) {
            return imageNode.get("url").asText();
        } else {
            return "null"; // Or set a default URL or handle it as needed
        }
    }

}
