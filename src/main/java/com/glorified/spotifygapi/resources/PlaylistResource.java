package com.glorified.spotifygapi.resources;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/playlists")
public class PlaylistResource {

    @GET
    public Response getUserPlaylists()
    {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/me/playlists?limit=50&offset=0");
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationResource.tokenManager.getToken().getAccessToken());

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return Response.ok(responseBody, MediaType.APPLICATION_JSON).build();
            }else{
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return Response.status(httpResponse.getStatusLine().getStatusCode()).entity(responseBody).build();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
