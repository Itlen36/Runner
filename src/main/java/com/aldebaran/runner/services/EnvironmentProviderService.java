package com.aldebaran.runner.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/env-provider")
@RegisterRestClient
public interface EnvironmentProviderService {

    @GET
    @Path("/get-env-id/{envName}")
    @Produces(MediaType.TEXT_PLAIN)
    UUID getEnvId(@PathParam String envName);
}
