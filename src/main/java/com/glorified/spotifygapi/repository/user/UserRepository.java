package com.glorified.spotifygapi.repository.user;

import com.glorified.spotifygapi.models.requests.LoginRequest;
import com.glorified.spotifygapi.models.user.User;

public interface UserRepository {
    User login(LoginRequest loginRequest);

    User createUser(User user);

    User findUser(String email);


}
