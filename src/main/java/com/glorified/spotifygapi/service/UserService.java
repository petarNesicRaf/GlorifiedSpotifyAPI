package com.glorified.spotifygapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.glorified.spotifygapi.models.requests.LoginRequest;
import com.glorified.spotifygapi.models.user.User;
import com.glorified.spotifygapi.repository.user.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import java.util.Date;

public class UserService {
    @Inject
    UserRepository userRepo;

    public String login(LoginRequest loginRequest)
    {
        String hashedPassword = DigestUtils.sha256Hex(loginRequest.getPassword());
        loginRequest.setPassword(hashedPassword);

        User user = this.userRepo.login(loginRequest);
        if(user == null || !user.getPassword().equals(hashedPassword))
        {
            return null;
        }
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24*60*60*1000);

        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(loginRequest.getEmail())
                .sign(algorithm);
    }

    public String createUser(User user)
    {
        String hashedPassword = DigestUtils.sha256Hex(user.getPassword());
        user.setPassword(hashedPassword);
        User createdUser = userRepo.createUser(user);
        if(createdUser == null)
        {
            return null;
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24*60*60*1000);

        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(createdUser.getEmail())

                .sign(algorithm);
    }

    public boolean isAuthorized(String token)
    {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        String username = jwt.getSubject();
        User user = this.userRepo.findUser(username);
        System.out.println("CLAIM " + jwt.getClaim("role").asString());

        if(user == null)
            return false;

        return true;
    }


}
