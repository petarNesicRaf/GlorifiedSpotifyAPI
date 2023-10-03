package com.glorified.spotifygapi;

import com.glorified.spotifygapi.repository.playlist.PlaylistRepoImpl;
import com.glorified.spotifygapi.repository.playlist.PlaylistRepository;
import com.glorified.spotifygapi.service.PlaylistService;
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

                this.bind(PlaylistRepoImpl.class).to(PlaylistRepository.class).in(Singleton.class);
                this.bindAsContract(PlaylistService.class);

            }
        };

        register(binder);

        packages("com.glorified.spotifygapi");
    }
}