package com.glorified.spotifygapi.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glorified.spotifygapi.models.AuthToken;
import com.glorified.spotifygapi.resources.AuthenticationResource;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


public class TokenManager {

    private AuthToken token;

    public TokenManager(AuthToken token) {
        this.token = token;
    }

    //with every api call this method should be called in case the accesstoken has expired
    public AuthToken getToken()
    {
        if (System.currentTimeMillis() >= token.getExpirationTime()) {
            // if the token has expired set a new auth token, note the refresh token stays the same
            // because it has a longer lifecycle
            setAccessToken();
        }
        return token;
    }

    // this method sets the new access token to the token manager
    private void setAccessToken()
    {
        Response response = getRefreshedToken();
        if(response.getStatus() == 200)
        {
            String responseBody = (String)response.getEntity();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                JsonNode accessNode = jsonNode.get("access_token");
                String access_token = accessNode.asText();

                JsonNode scopeNode = jsonNode.get("scope");
                String scope = scopeNode.asText();

                JsonNode expiresNode = jsonNode.get("expires_in");
                Long expires = expiresNode.asLong();

                String refreshToken = this.token.getRefreshToken();

                AuthToken token = new AuthToken(access_token,  refreshToken, scope, expires);

                setToken(token);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
    }

    //sends a http request to the spotify api to refresh the access_token
    private Response getRefreshedToken()
    {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost refreshPost = new HttpPost("https://accounts.spotify.com/api/token");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type","refresh_token"));
        params.add(new BasicNameValuePair("refresh_token", this.getToken().getRefreshToken()));

        refreshPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        try {
            refreshPost.setEntity(new UrlEncodedFormEntity(params));

            String authHeader = "Basic "  + base64Encode(AuthenticationResource.CLIENT_ID +":"+ AuthenticationResource.CLIENT_SECRET);

            refreshPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

            HttpResponse response = httpClient.execute(refreshPost);

            if(response.getStatusLine().getStatusCode() == 200)
            {
                String responseBody = EntityUtils.toString(response.getEntity());
                return Response.ok(responseBody, MediaType.APPLICATION_JSON).build();
            }else{
                return Response.status(response.getStatusLine().getStatusCode()).entity("Token refresh failed.").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }

    private String base64Encode(String input) {
        return java.util.Base64.getEncoder().encodeToString(input.getBytes());
    }

    public void setToken(AuthToken token) {
        this.token = token;
    }
}
