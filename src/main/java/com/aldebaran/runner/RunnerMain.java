package com.aldebaran.runner;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/runner")
public class RunnerMain {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String newRun() {
        RunData runData = new RunData(1L, 5L, 3L);
        runData.persist();
        if (runData.isPersistent())
            runData.delete();
        return "OK!";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public String hello() {
        return "Hello lol kek";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/GetAll")
    public String getAll() {
        StringBuilder str = new StringBuilder();
        List<RunData> AllRuns = RunData.listAll();
        for (RunData run : AllRuns) {
            str.append("RunID: ").append(run.getRunID()).append(" UserID: ").append(run.getUserID()).append(" CourseID: ").append(run.getCourseID()).append(" EnvID: ").append(run.getEnvironmentID()).append("\n");
        }
        return str.toString();
    }
}