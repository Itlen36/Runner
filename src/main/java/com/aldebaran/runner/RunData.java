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
    public UUID runID, userID, courseID, environmentID;
    public Long  step;
    public String status;
    public boolean checked, passed;
    public Time startTime;

    public RunData(UUID userId, UUID courseId, UUID environmentId) {
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

    public static RunData findRun(UUID userId, UUID courseId) {
        return find("userId = ?1 and courseId = ?2", userId, courseId).firstResult();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID getRunID() {
        return runID;
    }

    public void nextStep() {
        this.step++;
        this.passed = false;
        this.checked = false;
        this.status = "";
    }

    public UUID getUserID() {
        return userID;
    }

    public UUID getCourseID() {
        return courseID;
    }

    public UUID getEnvironmentID() {
        return environmentID;
    }

    public Long getStep() {return step;}

    public String getStatus() {
        return status;
    }

    public Time getStartTime() {
        return startTime;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public void setCourseID(UUID courseID) {
        this.courseID = courseID;
    }

    public void setEnvironmentID(UUID environmentID) {
        this.environmentID = environmentID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
