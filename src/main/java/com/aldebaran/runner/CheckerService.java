package com.aldebaran.runner;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RegisterRestClient
public interface CheckerService {
    @POST
    //@Produces(MediaType.)
    @Path("/check")
    public void check(ChecksDTO checksDTO);
}
