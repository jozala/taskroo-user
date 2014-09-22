package com.taskroo.user.service.filter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class InvitationCodeFilter implements ContainerRequestFilter {

    private final boolean invitationCodeEnabled;
    private final String invitationCodeValue;

    @Inject
    public InvitationCodeFilter(@Named("invitationCodeEnabled") Boolean invitationCodeEnabled,
                                @Named("invitationCodeValue") String invitationCodeValue) {
        this.invitationCodeEnabled = invitationCodeEnabled;
        this.invitationCodeValue = invitationCodeValue;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!invitationCodeEnabled) {
            return;
        }

        if (requestContext.getHeaderString("X-InvitationCode") == null ||
                !requestContext.getHeaderString("X-InvitationCode").equals(invitationCodeValue)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Correct invitation code is required")
                    .build());
        }
    }
}