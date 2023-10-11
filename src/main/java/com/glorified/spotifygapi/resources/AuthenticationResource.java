package com.glorified.spotifygapi.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glorified.spotifygapi.models.AuthToken;
import com.glorified.spotifygapi.utility.TokenManager;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

@Path("/token")
public class AuthenticationResource {

    public static final String CLIENT_ID = "2076850c72f841c1b16e1e789c8ed732";
    public static final String CLIENT_SECRET = "50e929cfb32c4d04bbffed7fcbb7e321";
    private static final String REDIRECT_URI = "http://localhost:8080/SpotifyGApi_war_exploded/api/token/callback";
    private static final String STATE_KEY = "spotify_auth_state";
    private static final Random RANDOM = new Random();

    public static TokenManager tokenManager;

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }


    @GET
    @Path("/login")
    public Response login() {
        //generise state
        String state = generateRandomString(16);
        NewCookie stateCookie = new NewCookie(STATE_KEY, state);
        //postavlja scope
        String encodedscope = URLEncoder.encode("user-read-private user-read-email");
        //postavljanje url-a uz query parametre koji su potrebni za dobijanje access token-a
        String authorizationUrl = "https://accounts.spotify.com/authorize?" +
                "response_type=code" +
                "&client_id=" + CLIENT_ID +
                "&scope=" + encodedscope +
                "&redirect_uri=" + REDIRECT_URI +
                "&state=" + state;
        //redirektuje na spotify login i salje 302 FOUND kod
        URI authURL = UriBuilder.fromUri(authorizationUrl).build();
        return Response.status(302).location(authURL).cookie(stateCookie).build();
    }

    //ovo je metoda koja ce se pozvati nakon logovanja
    //zacrtana je u aplikacije kao redirect url
    @GET
    @Path("/callback")
    public Response callback(
            @CookieParam(STATE_KEY) Cookie stateCookie,
            @QueryParam("code") String code,
            @QueryParam("state") String state) {

        if (stateCookie == null || state == null || !state.equals(stateCookie.getValue())) {
            URI errorUri = UriBuilder.fromUri("/#error=state_mismatch").build();
            return Response.status(302).location(errorUri).build();
        }

        List<NameValuePair> authParams = new ArrayList<>();
        authParams.add(new BasicNameValuePair("code", code));
        authParams.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
        authParams.add(new BasicNameValuePair("grant_type", "authorization_code"));

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost tokenRequest = new HttpPost("https://accounts.spotify.com/api/token");
        tokenRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + base64Encode(CLIENT_ID + ":" + CLIENT_SECRET));
        tokenRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

        try {
            tokenRequest.setEntity(new UrlEncodedFormEntity(authParams));
            org.apache.http.HttpResponse response = httpClient.execute(tokenRequest);
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());

                setAccessToken(responseBody);

                //access token treba da se vrati na front i uz svaki zahtev da se salje

                return Response.ok(responseBody, MediaType.APPLICATION_JSON).build();
            } else {
                URI errorUri = UriBuilder.fromUri("/#error=invalid_token").build();
                return Response.status(302).location(errorUri).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("An error occurred").build();
        }
    }

    private String generateRandomString(int length) {
        String possibleCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(possibleCharacters.length());
            randomString.append(possibleCharacters.charAt(randomIndex));
        }
        return randomString.toString();
    }

    private String base64Encode(String input) {
        return java.util.Base64.getEncoder().encodeToString(input.getBytes());
    }

    private void setAccessToken(String responseBody)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode accessTokenNode = jsonNode.get("access_token");
            String access_token = accessTokenNode.asText();
            JsonNode refreshTokenNode = jsonNode.get("refresh_token");
            String refresh_token = refreshTokenNode.asText();
            JsonNode scopeTokenNode = jsonNode.get("scope");
            String scope = scopeTokenNode.asText();
            JsonNode expirationTokenNode = jsonNode.get("expires_in");
            Long expiration = expirationTokenNode.asLong();

            AuthToken auth_token = new AuthToken(access_token, refresh_token, scope,expiration);
            tokenManager = new TokenManager(auth_token);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    @GET
    @Path("/refresh_token")
    public Response refreshToken(@QueryParam("refresh_token")String refreshToken)
    {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost refreshPost = new HttpPost("https://accounts.spotify.com/api/token");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type","refresh_token"));
        params.add(new BasicNameValuePair("refresh_token", auth_token.getRefreshToken()));

        refreshPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        try {
            refreshPost.setEntity(new UrlEncodedFormEntity(params));

            String authHeader = "Basic "  + base64Encode(CLIENT_ID +":"+CLIENT_SECRET);

            refreshPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

            HttpResponse response = httpClient.execute(refreshPost);

            if(response.getStatusLine().getStatusCode() == 200)
            {
                String responseBody = EntityUtils.toString(response.getEntity());
                return Response.ok(responseBody,MediaType.APPLICATION_JSON).build();
            }else{
                return Response.status(response.getStatusLine().getStatusCode()).entity("Token refresh failed.").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }

    }

     */
}

