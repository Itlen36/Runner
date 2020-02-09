package com.aldebaran.runner;

import io.vertx.core.json.Json;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jose4j.json.internal.json_simple.JSONObject;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
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
    @Path("/new-run/{courseID}")
    @Transactional
    public NewRunDTO newRun(String courseID) {
        UUID courseId = UUID.fromString(courseID);
        UUID userId = UUID.randomUUID();//jwt.claim("UserName");
        RunData existingRun = RunData.findRun(userId, courseId);
        RunData runData = new RunData();
        if (existingRun==null) {
            runData.setCourseID(courseId);
            runData.setUserID(userId);
            runData.setEnvironmentID(envProviderService.getEnvId(cmService.getEnvName(courseId)));
        } else {
            runData = existingRun;
        }
        NewRunDTO newRunDTO = new NewRunDTO();

        runData.persist();
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
        checkerService.check(checksDTO);
        return Response.ok().build();
    }







    /*@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/exampleUUID")
    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tx-json")
    public JSONObject testJSONtx() {
        RunData rd = new RunData();
        rd.setCourseID(228L);
        //Jsonb jb = new JsonbBuilder.create();
        JSONObject jsonObject = new JSONObject((Map) rd);
        //Json json = new Json(rd);
        //return cmService.sendJSON(jsonObject);
        return jsonObject;
    }*/

    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cm/{jsonO}")
    public JSONObject testJSON(@PathParam JSONObject jsonO) {
        return jsonO;
    }*/

   /* private Set<RunData> runDataSet = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/cm/sendjson")
    public RunData getRun(RunData rd) {
        //Set<RunData> runDataSet = new HashSet<>();
        rd.setUserID(228L);
        runDataSet.add(rd);
        return rd;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("send-run")
    public RunData SendJSON() {
        RunData runData = new RunData(21L,22L,23L);
        return cmService.sendRUN(runData);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public String hello() {
        System.out.println("hello -------------");
        return "Hello lol kek";
    }
*/
 /*   @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/jwt")
    @PermitAll
    public JsonWebToken getJWT() {
        return jwt;
    }*/
/*
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/delete/{id}")
    @PermitAll
    @Transactional
    public String delete(@PathParam Long id) {
        RunData.delete("runID", id);
        return "OK!";
    }
*/
    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-checks/{courseId}/{step}")
    public Set<RunData> getChecks(@PathParam Long courseId, @PathParam int step) {
        Set<RunData> runData = cmService.getChecks(courseId, step);
        return runData;
    }*/
  /*  @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-checks/{courseId}/{step}")
    public Json getChecks(@PathParam Long courseId, @PathParam Long step) {
        //JSONObject runData = cmService.getChecks(courseId, step);
        Json runData = cmService.getChecks(courseId, step);
        return runData;
    }*/

    /*@GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Country name(@PathParam String name) {
        return cmService.getCapital(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cm/get-checks/228/4")
    public RunData test() {
        RunData rd = new RunData();
        rd.setCourseID(2281488L);
        Set<RunData> rdS = new HashSet<>();
        rdS.add(rd);
        return rd;
    }*/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/AddRun")
    @Transactional
    public RunData addRun() {
        RunData rd = new RunData();
        rd.setCourseID(UUID.randomUUID());
        rd.setEnvironmentID(UUID.randomUUID());
        rd.setUserID(UUID.randomUUID());
        rd.persist();
        return  rd;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/get-all")
    public String getAll() {
        StringBuilder str = new StringBuilder();
        List<RunData> AllRuns = RunData.listAll();
        if (AllRuns.isEmpty())
            str.append("Empty");
        for (RunData run : AllRuns) {
            str.append("RunID: ").append(run.getRunID())
                    .append(" UserID: ").append(run.getUserID())
                    .append(" CourseID: ").append(run.getCourseID())
                    .append(" EnvID: ").append(run.getEnvironmentID())
                    .append(" Step ").append(run.getStep())
                    .append(" Time ").append(run.getStartTime()).append("\n");
        }
        return str.toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/getAll")
    public RunData getAll2() {
        Set<RunData> runDataSet = new HashSet<>();
        List<RunData> runDataList = RunData.listAll();
        /*for (RunData rd :  runDataList)
            runDataSet.add(rd);*/
        runDataSet.add(runDataList.get(0));
        return runDataList.get(0);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getFirst")
    public NewRunDTO getFirst() {
        List<RunData> runDataList = RunData.listAll();
        NewRunDTO newRunDTO = new NewRunDTO();
        newRunDTO.setEnvironmentId(runDataList.get(0).getEnvironmentID());
        newRunDTO.setRunId(runDataList.get(0).getRunID());
        return newRunDTO;
    }
}