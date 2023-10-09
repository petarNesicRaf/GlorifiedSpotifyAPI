package com.glorified.spotifygapi;

import com.glorified.spotifygapi.repository.playlist.PlaylistRepoImpl;
import com.glorified.spotifygapi.repository.playlist.PlaylistRepository;
import com.glorified.spotifygapi.repository.tracks.TrackRepoImpl;
import com.glorified.spotifygapi.repository.tracks.TrackRepository;
import com.glorified.spotifygapi.repository.user.UserRepoImpl;
import com.glorified.spotifygapi.repository.user.UserRepository;
import com.glorified.spotifygapi.service.PlaylistService;
import com.glorified.spotifygapi.service.TrackService;
import com.glorified.spotifygapi.service.UserService;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class Application extends ResourceConfig {
    public Application(){
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {

                this.bind(UserRepoImpl.class).to(UserRepository.class).in(Singleton.class);
                this.bindAsContract(UserService.class);

                this.bind(PlaylistRepoImpl.class).to(PlaylistRepository.class).in(Singleton.class);
                this.bindAsContract(PlaylistService.class);

                this.bind(TrackRepoImpl.class).to(TrackRepository.class).in(Singleton.class);
                this.bindAsContract(TrackService.class);

            }
        };

        register(binder);

        packages("com.glorified.spotifygapi");
    }
}