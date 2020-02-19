package com.aldebaran.runner.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class CheckerReply extends PanacheEntity {
    public Long id;
    public Long requestsCounter;
    public Time lastReplyTime;
    public Date lastReplyDate;
    public boolean allowRepeatedRequests;

    public CheckerReply() {
        this.requestsCounter = 0L;
        setTime();
        this.allowRepeatedRequests = false;
    }

    public static CheckerReply getCheckerReply() {
        CheckerReply checkerReply = CheckerReply.findById(1L);
        if (checkerReply == null)
            checkerReply = new CheckerReply();
        return checkerReply;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void incrementRequestsCounter() {
        this.requestsCounter++;
    }

    public void decrementRequestsCounter() {
        this.requestsCounter--;
    }

    public void setTime() {
        this.lastReplyTime = Time.valueOf(LocalTime.now());
        this.lastReplyDate = Date.valueOf(LocalDate.now());
    }

    public Long getRequestsCounter() {
        return requestsCounter;
    }

    public Time getLastReplyTime() {
        return lastReplyTime;
    }

    public Date getLastReplyDate() {
        return lastReplyDate;
    }

    public boolean isAllowRepeatedRequests() {
        return allowRepeatedRequests;
    }

    public void setAllowRepeatedRequests(boolean allowRepeatedRequests) {
        this.allowRepeatedRequests = allowRepeatedRequests;
    }
}
