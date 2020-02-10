package com.aldebaran.runner.services;

import com.aldebaran.runner.dto.ChecksDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.awt.*;

@RegisterRestClient
public interface CheckerService {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/check")
    public void check(ChecksDTO checksDTO);
}
