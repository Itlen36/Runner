package com.aldebaran.runner.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
public class RunData extends PanacheEntity {

    public UUID runID;
    public UUID userID;
    public UUID courseID;
    public UUID environmentID;
    public Long  step;
    public String status;
    public boolean checked;
    public boolean passed;
    public boolean checking;
    public Time startTime;
    public Date startDate;

    public RunData(UUID userId, UUID courseId, UUID environmentId) {
        userID = userId;
        courseID = courseId;
        environmentID = environmentId;
        step = 1L;
        startTime = Time.valueOf(LocalTime.now());
        startDate = Date.valueOf(LocalDate.now());
    }

    public RunData() {
        step = 1L;
        startTime = Time.valueOf(LocalTime.now());
        startDate = Date.valueOf(LocalDate.now());
    }

    public RunData(boolean val) {
        step = 1L;
        startTime = Time.valueOf(LocalTime.now());
        startDate = Date.valueOf(LocalDate.now());
        checked = false;
        passed = false;
        status = "";
        checking = val;
        userID = UUID.randomUUID();
        courseID = UUID.randomUUID();
        environmentID = UUID.randomUUID();
    }

    public static RunData findRun(UUID userId, UUID courseId) {
        return find("userId = ?1 and courseId = ?2", userId, courseId).firstResult();
    }

    public static List<RunData> getAwaitingCheck() {
        return list("checking", Sort.by("startDate").and("startTime"), true);
    }

    public void nextStep() {
        this.step++;
        this.passed = false;
        this.checked = false;
        this.checking = false;
        this.status = "";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID getRunID() {
        return runID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UUID getCourseID() {
        return courseID;
    }

    public void setCourseID(UUID courseID) {
        this.courseID = courseID;
    }

    public UUID getEnvironmentID() {
        return environmentID;
    }

    public void setEnvironmentID(UUID environmentID) {
        this.environmentID = environmentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isChecking() {
        return checking;
    }

    public void setChecking(boolean checking) {
        this.checking = checking;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Long getStep() {
        return step;
    }
}
