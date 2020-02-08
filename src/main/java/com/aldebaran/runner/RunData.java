package com.aldebaran.runner;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.vertx.core.json.Json;
import org.jose4j.json.internal.json_simple.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Time;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class RunData extends PanacheEntity {
    public UUID runID;
    public Long userID, courseID, environmentID, step;
    public String status;
    public Time startTime;

    public RunData(Long userId, Long courseId, Long environmentId) {
        userID = userId;
        courseID = courseId;
        environmentID = environmentId;
        step = 1L;
        startTime = Time.valueOf(LocalTime.now());
    }

    public RunData() {
        step = 1L;
        startTime = Time.valueOf(LocalTime.now());
    }

    public static RunData findRun(Long userId, Long courseId) {
        return find("userid = ?1 and courseid = ?2", userId, courseId).firstResult();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public UUID getRunID() {
        return runID;
    }

    public void nextStep() {
        step++;
    }

    public Long getUserID() {
        return userID;
    }

    public Long getCourseID() {
        return courseID;
    }

    public Long getEnvironmentID() {
        return environmentID;
    }

    public Long getStep() {return step;}

    public String getStatus() {
        return status;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    public void setEnvironmentID(Long environmentID) {
        this.environmentID = environmentID;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
