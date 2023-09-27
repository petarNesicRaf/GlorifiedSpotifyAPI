package com.glorified.spotifygapi.resources;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/user")
public class UserResource {
    private final String CLIENT_ID = "2076850c72f841c1b16e1e789c8ed732";
    private final String SECRET_ID = "50e929cfb32c4d04bbffed7fcbb7e321";

    private final String LIL_TJAY = "6jGMq4yGs7aQzuGsMgVgZR";
    @GET
    @Path("/profile")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getProfile(){
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/me");
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationResource.tokenManager.getToken().getAccessToken());

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return Response.ok(responseBody, MediaType.APPLICATION_JSON).build();
            }else if(httpResponse.getStatusLine().getStatusCode() == 401)
            {
                return Response.status(401).entity("Bad or expired token. This can happen if the user revoked a token or the access token has expired. You should re-authenticate the user.").build();
            }else if(httpResponse.getStatusLine().getStatusCode() == 403){
                return Response.status(403).entity("Bad OAuth request (wrong consumer key, bad nonce, expired timestamp...). Unfortunately, re-authenticating the user won't help here.").build();
            }else{
                return Response.status(429).entity("The app has exceeded its rate limits.").build();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
