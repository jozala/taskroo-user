package com.taskroo.user.server;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class UserServiceApp extends ResourceConfig {
    public UserServiceApp() {
        register(ExceptionListener.class);
        register(RolesAllowedDynamicFeature.class);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        packages("com.taskroo.user.service", "com.wordnik.swagger.jersey.listing");
    }
}
