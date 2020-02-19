package com.aldebaran.runner.dto;

import java.util.UUID;

public class NewRunDTO {
    private UUID runId, environmentId;

 public void setRunId(UUID runId) {
        this.runId = runId;
    }

    public void setEnvironmentId(UUID environmentId) {
        this.environmentId = environmentId;
    }

    public UUID getRunId() {
        return runId;
    }

    public UUID getEnvironmentId() {
        return environmentId;
    }
}
