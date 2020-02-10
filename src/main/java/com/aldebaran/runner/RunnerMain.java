package com.aldebaran.runner;


import com.aldebaran.runner.dto.ChecksDTO;
import com.aldebaran.runner.dto.NewRunDTO;
import com.aldebaran.runner.dto.ReturnCheckStatusDTO;
import com.aldebaran.runner.services.CMService;
import com.aldebaran.runner.services.CheckerService;
import com.aldebaran.runner.services.EnvironmentProviderService;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/runner")
@RequestScoped
public class RunnerMain {

    @Inject
    @RestClient
    CMService cmService;
    @Inject
    @RestClient
    EnvironmentProviderService envProviderService;
    @Inject
    @RestClient
    CheckerService checkerService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/new-run")
    @Transactional
    public NewRunDTO newRun(String courseID) {
        UUID courseId = UUID.fromString(courseID);
        UUID userId = UUID.randomUUID();//jwt.claim("UserName");
        RunData existingRun = RunData.findRun(userId, courseId);
        RunData runData = new RunData();
        if (existingRun == null) {
            runData.setCourseID(courseId);
            runData.setUserID(userId);
            runData.setEnvironmentID(envProviderService.getEnvId(cmService.getEnvName(courseId)));
            runData.persist();
        } else {
            runData = existingRun;
        }
        NewRunDTO newRunDTO = new NewRunDTO();
        newRunDTO.setRunId(runData.getRunID());
        newRunDTO.setEnvironmentId(runData.getEnvironmentID());
        return newRunDTO;
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/check")
    public Response check(String runId) {
        RunData runData = RunData.findById(UUID.fromString(runId));
        if (runData == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        ChecksDTO checksDTO = new ChecksDTO();
        checksDTO.setChecks(cmService.getChecks(runData.getCourseID(), runData.getStep()));
        checksDTO.setEnvironmentId(runData.getEnvironmentID());
        checkerService.check(checksDTO);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/next-step")
    public Response nextStep(String runId) {
        RunData runData = RunData.findById(UUID.fromString(runId));
        if (runData == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if (runData.isChecked()) {
            if (runData.isPassed()) {
                runData.nextStep();
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.PRECONDITION_FAILED) //412
                        .entity(runData.getStatus())
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }
        } else
            return Response.status(Response.Status.ACCEPTED).build(); //202
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/return-check-status")
    public Response returnCheckStatus(ReturnCheckStatusDTO returnCheckStatusDTO) {
        RunData runData = RunData.findById(returnCheckStatusDTO.getRunId());
        if (runData == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        runData.setPassed(returnCheckStatusDTO.isPassed());
        runData.setStatus(returnCheckStatusDTO.getStatus());
        runData.setChecked(true);
        return Response.ok().build();
    }

    @GET
    @Path("runs/{runId}/env-id")
    public Response getEnvironmentId(@PathParam String runId) {
        RunData runData = RunData.findById(runId);
        if (runData == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok()
                .entity(runData.getEnvironmentID().toString())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

}