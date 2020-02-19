package com.aldebaran.runner.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/cm")
@RegisterRestClient
public interface CMService {
    @GET
    @Path("/courses/{courseId}/env-name")
    @Produces(MediaType.TEXT_PLAIN)
    String getEnvName(@PathParam String courseId);

    @GET
    @Path("/courses/{courseId}/steps/{step}/checks")
    @Produces(MediaType.TEXT_PLAIN)
    byte[] getChecks(@PathParam String courseId, @PathParam Long step);

    @GET
    @Path("/courses/{courseId}/steps/number")
    @Produces(MediaType.TEXT_PLAIN)
    String getNumber(@PathParam String courseId);
}
