package com.aldebaran.runner;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.vertx.core.json.Json;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Time;

@Entity
public class RunData extends PanacheEntity {
    public Long runID, userID, courseID, environmentID;
    public int step;
    //public Time startTime;
    //public Json SetOfChecks;

    public RunData(Long userId, Long courseId, Long environmentId) {
        userID = userId;
        courseID = courseId;
        environmentID = environmentId;
        step = 0;
    }

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getRunID() {
        return runID;
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

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    public void setEnvironmentID(Long environmentID) {
        this.environmentID = environmentID;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
