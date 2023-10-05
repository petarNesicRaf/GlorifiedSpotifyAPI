package com.glorified.spotifygapi.resources;

import com.glorified.spotifygapi.models.track.TrackFeatures;
import com.glorified.spotifygapi.service.TrackService;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/tracks")
public class TrackResource {

    @Inject
    TrackService trackService;

    @GET
    @Path("/analysis")
    public Response getTracksAnalysis(@QueryParam("id") List<String> ids)
    {
        //String[] IDs = id.split(",");
        //List<String> listIDs = Arrays.asList(IDs);
        String list = "ids=";
        for(String s:ids)
        {

            list += s;
            list +=",";
        }
        list = list.substring(0, list.length()-1);
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/audio-features?"+list);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationResource.tokenManager.getToken().getAccessToken());

        try {
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == 200)
            {
                List<TrackFeatures> features = trackService.insertTrackFeatures(responseBody, ids);
                return Response.ok(features, MediaType.APPLICATION_JSON).build();
            }else{
                return Response.status(response.getStatusLine().getStatusCode()).entity(responseBody).build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
