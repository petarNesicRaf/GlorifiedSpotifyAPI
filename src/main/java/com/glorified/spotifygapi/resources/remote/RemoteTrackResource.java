package com.glorified.spotifygapi.resources.remote;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glorified.spotifygapi.models.track.Track;
import com.glorified.spotifygapi.resources.AuthenticationResource;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/remote-tracks")
public class RemoteTrackResource {

    @GET
    @Path("/playlist/{id}")
    public Response fetchTracksFromPlaylist(@PathParam("id") String id)
    {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/playlists/" + id + "/tracks");
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationResource.tokenManager.getToken().getAccessToken());

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                ObjectMapper objectMapper =new ObjectMapper();
                JsonNode tree = objectMapper.readTree(responseBody);
                JsonNode items = tree.get("items");

                List<Track> tracks = new ArrayList<>();

                for(JsonNode item:items)
                {
                    Track track = deserializeTracks(item);
                    tracks.add(track);
                }
                return Response.ok(tracks, MediaType.APPLICATION_JSON).build();
            } else {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return Response.status(httpResponse.getStatusLine().getStatusCode()).entity(responseBody).build();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    private String getImageUrlOrDefault(JsonNode imageNode, String size) {
        if (imageNode != null && !imageNode.isMissingNode() && imageNode.has("url")) {
            return imageNode.get("url").asText();
        } else {
            return "null"; // Or set a default URL or handle it as needed
        }
    }


}
