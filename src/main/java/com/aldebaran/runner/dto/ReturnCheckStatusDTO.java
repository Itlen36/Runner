package com.aldebaran.runner.dto;

import java.util.UUID;

public class ReturnCheckStatusDTO {
    private UUID runId;
    private boolean passed;
    private String Status;

    public ReturnCheckStatusDTO() {

    }

    public UUID getRunId() {
        return runId;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getStatus() {
        return Status;
    }
}
