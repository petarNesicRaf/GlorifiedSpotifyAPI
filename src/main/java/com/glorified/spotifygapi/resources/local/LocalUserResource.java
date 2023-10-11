package com.glorified.spotifygapi.resources.local;

import com.glorified.spotifygapi.models.requests.LoginRequest;
import com.glorified.spotifygapi.models.user.User;
import com.glorified.spotifygapi.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/local-user")
public class LocalUserResource {

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(@Valid LoginRequest loginRequest)
    {
        Map<String, String> response = new HashMap<>();
        String jwt = this.userService.login(loginRequest);
        if(jwt == null)
        {
            response.put("message", "These credentials do not match out records (Login)");
            return Response.status(422, "Unprocessable entity").entity(response).build();
        }


        response.put("jwt", jwt);
        return Response.ok(response).build();
    }

    @POST
    @Path("/create")
    @Produces({MediaType.APPLICATION_JSON})
    public Response createUser(@Valid User user)
    {
        Map<String, String> response = new HashMap<>();
        String jwt = this.userService.createUser(user);
        if(jwt == null)
        {
            response.put("message", "These credentials do not match out records (Login)");
            return Response.status(422, "Unprocessable entity").entity(response).build();
        }
        response.put("jwt", jwt);
        return Response.ok(response).build();
    }

}
