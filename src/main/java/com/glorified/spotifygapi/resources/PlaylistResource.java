package com.glorified.spotifygapi.resources;

import com.glorified.spotifygapi.models.playlist.Playlist;
import com.glorified.spotifygapi.service.PlaylistService;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/playlists")
public class PlaylistResource {

    @Inject
    PlaylistService playlistService;

    @GET
    public Response fetchAndInsert() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/me/playlists?limit=50&offset=0");
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationResource.tokenManager.getToken().getAccessToken());

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());

                playlistService.deleteInsertPlaylist(responseBody);

                List<Playlist> allPlaylists = this.playlistService.getAllPlaylist();
                return Response.ok(allPlaylists, MediaType.APPLICATION_JSON).build();

            } else {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return Response.status(httpResponse.getStatusLine().getStatusCode()).entity(responseBody).build();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/playlist_json")
    public Response getPlaylist() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/me/playlists?limit=50&offset=0");
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationResource.tokenManager.getToken().getAccessToken());

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());

                return Response.ok(responseBody, MediaType.APPLICATION_JSON).build();

            } else {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return Response.status(httpResponse.getStatusLine().getStatusCode()).entity(responseBody).build();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/tracks/{name}")
    public Response getTracksFromPlaylist(@PathParam("name") String playlistName)
    {
        String playlistID = playlistService.getPlaylistID(playlistName);
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(playlistID);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationResource.tokenManager.getToken().getAccessToken());

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());

                return Response.ok(responseBody, MediaType.APPLICATION_JSON).build();

            } else {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return Response.status(httpResponse.getStatusLine().getStatusCode()).entity(responseBody).build();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
