package com.aldebaran.runner.dto;

import java.util.UUID;

public class ChecksDTO {
    private byte[] checks;
    private UUID environmentId;
    private String returnPoint;
    public ChecksDTO () {

    }

    public void setChecks(byte[] checks) {
        this.checks = checks;
    }

    public void setEnvironmentId(UUID environmentId) {
        this.environmentId = environmentId;
    }

    public void setReturnPoint(String returnPoint) {
        this.returnPoint = returnPoint;
    }
}
