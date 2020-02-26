package com.aldebaran.runner;


import com.aldebaran.runner.dto.ChecksDTO;
import com.aldebaran.runner.dto.NewRunDTO;
import com.aldebaran.runner.dto.ReturnCheckStatusDTO;
import com.aldebaran.runner.model.CheckerReply;
import com.aldebaran.runner.model.Course;
import com.aldebaran.runner.model.RunData;
import com.aldebaran.runner.services.CMService;
import com.aldebaran.runner.services.CheckerService;
import com.aldebaran.runner.services.EnvironmentProviderService;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalTime;
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
        UUID userId = jwt.getClaim("UserName");
        RunData existingRun = RunData.findRun(userId, courseId);
        RunData runData = new RunData();
        if (existingRun == null) {
            runData.setCourseID(courseId);
            runData.setUserID(userId);
            String envName = cmService.getEnvName(courseId.toString());
            runData.setEnvironmentID(UUID.fromString(envProviderService.getEnvId(envName)));
            runData.persist();
            Course course = Course.findById(courseId);
            if (course == null) {
                course = new Course(courseId);
                course.setNumberStep(Long.getLong(cmService.getNumber(courseId.toString())));
            } else
                course.incrementCounter();
            course.persist();
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
    @Path("/next-step")
    @Transactional
    public Response nextStep(String runId) {
        RunData runData = RunData.findById(UUID.fromString(runId));
        if (runData == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if (!runData.isChecking() && !runData.isChecked()) {
            return sendChecks(runData, true);
        } else {
            return checkCondition(runData);
        }
    }

    public Response sendChecks(RunData runData, boolean initialRequest) {
        ChecksDTO checksDTO = new ChecksDTO();
        checksDTO.setChecks(cmService.getChecks(runData.getCourseID().toString(), runData.getStep()));
        checksDTO.setEnvironmentId(runData.getEnvironmentID());
        checksDTO.setReturnPoint("runner/runs/" + runData.runID + "/checked");
        checkerService.check(checksDTO);
        if (initialRequest) {
            CheckerReply checkerReply = CheckerReply.getCheckerReply();
            checkerReply.incrementRequestsCounter();
            checkerReply.persist();
        }
        runData.setChecking(true);
        runData.persist();
        return Response.ok().build();
    }

    private Response checkCondition(RunData runData) {
        if (runData.isChecked()) {
            if (runData.isPassed()) {
                Course course = Course.findById(runData.getCourseID());
                Long numberStep = course.getNumberStep();
                if (runData.step.equals(numberStep)) {
                    if (course.counter == 1L)
                        course.delete();
                    else {
                        course.decrementCounter();
                        course.persist();
                    }
                    envProviderService.deleteEnvironment(runData.getEnvironmentID().toString());
                    runData.delete();
                    return Response.ok()
                            .entity(++numberStep)
                            .type(MediaType.TEXT_PLAIN)
                            .build();
                } else {
                    runData.nextStep();
                    runData.persist();
                    return Response.ok()
                            .entity(runData.getStep())
                            .type(MediaType.TEXT_PLAIN)
                            .build();
                }
            } else {
                runData.setChecking(false);
                runData.setChecked(false);
                runData.setStatus("");
                runData.persist();
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
    @Path("/runs/{runId}/checked")
    @Transactional
    public Response returnCheckStatus(@PathParam String runId, ReturnCheckStatusDTO returnCheckStatusDTO) {
        RunData runData = RunData.findById(UUID.fromString(runId));
        if (runData == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if (!runData.isChecking())
            return Response.status(Response.Status.NOT_FOUND).build();
        CheckerReply checkerReply = CheckerReply.getCheckerReply();
        checkerReply.decrementRequestsCounter();
        checkerReply.setTime();
        checkerReply.persist();
        runData.setPassed(returnCheckStatusDTO.isPassed());
        runData.setStatus(returnCheckStatusDTO.getStatus());
        runData.setChecked(true);
        runData.setChecking(false);
        runData.persist();
        return Response.ok().build();
    }

    @GET
    @Path("/runs/{runId}/env-id")
    public Response getEnvironmentId(@PathParam String runId) {
        RunData runData = RunData.findById(UUID.fromString(runId));
        if (runData == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok()
                .entity(runData.getEnvironmentID().toString())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    //---------------------------------------
    //For Testing
    //---------------------------------------

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public String hello() {
        return "Hello";
    }

    @GET
    @Path("/cm/courses/{courseId}/env-name")
    @Produces(MediaType.TEXT_PLAIN)
    public String getEnvName(@PathParam String courseId) {
        return "name";
    }

    @GET
    @Path("/env-provider/get-env-id/{envName}")
    @Produces(MediaType.TEXT_PLAIN)
    public UUID getEnvId(@PathParam String envName) {
        return UUID.randomUUID();
    }

    @GET
    @Path("/cm/courses/{courseId}/steps/number")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNumber(@PathParam String courseId) {
        return String.valueOf(5);
    }

    @GET
    @Path("/cm/courses/{courseId}/steps/{step}/checks")
    @Produces(MediaType.TEXT_PLAIN)
    public byte[] getChecks(@PathParam String courseId, @PathParam Long step) {
        return "test".getBytes();
    }

    Logger LOG = LoggerFactory.getLogger(RunnerMain.class);
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/checker/check")
    public Response check(ChecksDTO checksDTO) {
        LOG.info("check");
        return Response.ok().build();
    }

    @GET
    @Path("/allow-repeated-requests/{val}")
    @Transactional
    public Response allowRepeatedRequests(@PathParam boolean val) {
        CheckerReply checkerReply = CheckerReply.getCheckerReply();
        if (checkerReply.isAllowRepeatedRequests() == val) {
            return Response.status(Response.Status.ACCEPTED).build();
        } else
        {
            checkerReply.setAllowRepeatedRequests(val);
            checkerReply.persist();
            return Response.ok().build();
        }
    }
}