package com.aldebaran.runner.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Course extends PanacheEntity {

    public UUID courseID;
    public Long numberStep, counter;

    public Course(UUID courseID) {
        this.courseID = courseID;
        this.counter = 1L;
    }
    public Course() {

    }

    @Id
    public UUID getCourseID() {
        return courseID;
    }

    public void setCourseID(UUID courseID) {
        this.courseID = courseID;
    }

    public Long getNumberStep() {
        return numberStep;
    }

    public void setNumberStep(Long numberStep) {
        this.numberStep = numberStep;
    }

    public Long getCounter() {
        return counter;
    }

    public void incrementCounter() {
        this.counter++;
    }

    public void decrementCounter() {
        this.counter--;
    }
}
