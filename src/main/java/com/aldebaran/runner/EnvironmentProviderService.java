package com.aldebaran.runner;

import io.vertx.core.json.Json;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jose4j.json.internal.json_simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.UUID;

@Path("/env-provider")
@RegisterRestClient
public interface EnvironmentProviderService {

    @GET
    @Path("/get-env-id/{envName}")
    @Produces(MediaType.TEXT_PLAIN)
    UUID getEnvId(@PathParam String envName);
    /*@GET
    @Path("/get-checks/{courseId}/{step}")
    @Produces("application/json")
    Set<RunData> getChecks(@PathParam Long courseId, @PathParam int step);*/
    /*@GET
    @Path("/get-checks/{courseId}/{step}")
    @Produces("application/json")
    JSONObject getChecks(@PathParam Long courseId, @PathParam int step);*/


    /*
    @GET
    @Path("/get-checks/{courseId}/{step}")
    @Produces("application/json")
    Json getChecks(@PathParam Long courseId, @PathParam Long step);

    @POST
    @Path("/sendjson")
    @Produces("application/json")
    @Consumes("application/json")
    RunData sendRUN(RunData rd);

    @GET
    @Path("/get-image/{courseId}")
    @Produces("application/json")
    JSONObject getImage(@PathParam Long courseId);

    @GET
    @Path("/tx-json/{jsonO}")
    @Produces("application/json")
    JSONObject sendJSON(@PathParam JSONObject jsonO);

    @GET
    @Path("/name/{name}")
    @Produces("application/json")
    Country getCapital(@PathParam String name);*/
}
